package acme.features.inventor.chimpum;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.entities.configuration.Configuration;
import acme.entities.item.Item;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Inventor;
import acme.utils.AcceptedCurrencyLibrary;
import main.AntiSpam;

@Service
public class InventorChimpumCreateService implements AbstractCreateService<Inventor, Chimpum>{
	
	@Autowired
	protected InventorChimpumRepository repository;
	
	@Autowired
	protected InventorItemRepository inventorItemRepository;

	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;
		
		boolean result;
		
		final int itemId;
		final Item item;
		final Inventor inventor;
		
		itemId = request.getModel().getInteger("itemId");
		item = this.inventorItemRepository.findItemByItemId(itemId);
		
		inventor = item.getInventor();
		
		result = request.isPrincipal(inventor) && this.repository.findChimpumByItemId(itemId)==null;
		
		return result;
	}

	@Override
	public void bind(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors,"code", "title", "description", "creationMoment", "startDate", "finishDate", "budget", "link");
		
		
	}

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model,"code", "title", "description", "creationMoment", "startDate", "finishDate", "budget","link");
		
		model.setAttribute("itemName", this.inventorItemRepository.findItemByItemId(request.getModel().getInteger("itemId")).getName());
		model.setAttribute("itemId", request.getModel().getInteger("itemId"));
	}

	@Override
	public Chimpum instantiate(final Request<Chimpum> request) {
		assert request != null;
		
		int itemId;
		Item item;
		
		itemId = request.getModel().getInteger("itemId");
		
		item = this.repository.findItemByItemId(itemId);
		
		final Chimpum result = new Chimpum();
		
		result.setCreationMoment(new Date());
		
		result.setItem(item);
		
		return result;
	}

	@Override
	public void validate(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		final List<String> acceptedCurrencies = AcceptedCurrencyLibrary.getAcceptedCurrencies(this.inventorItemRepository.findAcceptedCurrencies());
		
		boolean spamWord;
		final boolean spamWordTitle;
		
		final Configuration configuration = this.inventorItemRepository.configuration();
		final AntiSpam antiSpam = new AntiSpam(configuration.getStrongSpamWords(), configuration.getStrongSpamThreshold(), configuration.getWeakSpamWords(), configuration.getWeakSpamThreshold(), entity.getDescription());
		spamWord = antiSpam.getAvoidSpam();
		errors.state(request, !spamWord, "description", "inventor.chimpum.form.error.spamWord");
		
		final AntiSpam antiSpamTitle = new AntiSpam(configuration.getStrongSpamWords(), configuration.getStrongSpamThreshold(), configuration.getWeakSpamWords(), configuration.getWeakSpamThreshold(), entity.getTitle());
		spamWordTitle = antiSpamTitle.getAvoidSpam();
		errors.state(request, !spamWordTitle, "title", "inventor.chimpum.form.error.spamWord");
		
		if(!(request.getModel().hasAttribute("defaultCurrency"))){
			
			if(!errors.hasErrors("budget")) {
				boolean acceptedCurrency;
				
				acceptedCurrency = acceptedCurrencies.contains(entity.getBudget().getCurrency());
				
				errors.state(request, acceptedCurrency, "budget", "inventor.chimpum.form.error.acceptedCurrency");
				
				boolean positiveValue;
				
				positiveValue = entity.getBudget().getAmount()>0;
				
				errors.state(request, positiveValue, "budget", "inventor.chimpum.form.error.positiveValue");
			}
			
		}else {
			
			if(!errors.hasErrors("defaultCurrency")) {
				boolean acceptedCurrency;
				
				acceptedCurrency = acceptedCurrencies.contains(entity.getBudget().getCurrency());
				
				errors.state(request, acceptedCurrency, "defaultCurrency", "inventor.chimpum.form.error.acceptedCurrency");
				
				boolean positiveValue;
				
				positiveValue = entity.getBudget().getAmount()>0;
				
				errors.state(request, positiveValue, "defaultCurrency", "inventor.chimpum.form.error.positiveValue");
			}
			
		}
		
		if (!errors.hasErrors("startDate")) {
			
			final Calendar calendar = Calendar.getInstance();
			Date minimumPeriodStart;
			
			calendar.setTime(entity.getCreationMoment());
			calendar.add(Calendar.MONTH, 1);
			minimumPeriodStart = calendar.getTime();
			
			
			errors.state(request, entity.getStartDate().after(minimumPeriodStart), "startDate", "inventor.chimpum.form.error.acceptedPeriodTime.start");
			
		}
		
		if (!errors.hasErrors("finishDate")) {
			
			final Calendar calendar = Calendar.getInstance();
			Date minimumPeriodFinish;
			
			calendar.setTime(entity.getStartDate());
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			minimumPeriodFinish = calendar.getTime();
			
			
			errors.state(request, entity.getFinishDate().after(minimumPeriodFinish), "finishDate", "inventor.chimpum.form.error.acceptedPeriodTime.finish");
		}
		
		if (!errors.hasErrors("code")) {
			
			final Calendar c = new GregorianCalendar();
			c.setTime(entity.getCreationMoment());
			
			final int yearInt = c.get(Calendar.YEAR);
			
			final String yearString = String.valueOf(yearInt).substring(2);
			
			String monthString = "";
			if(c.get(Calendar.MONTH) < 9) {
				 monthString += "0"+ (c.get(Calendar.MONTH) + 1);
			}else {
				monthString += c.get(Calendar.MONTH);
			}
			
			String dayString = "";
			if(c.get(Calendar.DAY_OF_MONTH) < 9) {
				dayString += "0"+ (c.get(Calendar.DAY_OF_MONTH));
			}else {
				dayString += c.get(Calendar.DAY_OF_MONTH);
			}
			
			final String codeYear = entity.getCode().split("-")[0].substring(0,2);
			final String codeMonth = entity.getCode().split("-")[0].substring(2,4);
			final String codeDay = entity.getCode().split("-")[0].substring(4,6);
			
			errors.state(request, codeYear.equals(yearString) && codeMonth.equals(monthString) && codeDay.equals(dayString) , "code", "inventor.chimpum.form.error.invalidCode");
			
		}
		
	}

	@Override
	public void create(final Request<Chimpum> request, final Chimpum entity) {
		assert request != null;
		assert entity != null;
				
		this.repository.save(entity);
		
	}
	
	

}
