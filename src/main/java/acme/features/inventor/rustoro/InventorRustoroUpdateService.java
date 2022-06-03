package acme.features.inventor.rustoro;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.rustoro.Rustoro;
import acme.entities.configuration.Configuration;
import acme.entities.item.Item;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Inventor;
import acme.utils.AcceptedCurrencyLibrary;
import acme.utils.ChangeCurrencyLibrary;
import main.AntiSpam;

@Service
public class InventorRustoroUpdateService implements AbstractUpdateService<Inventor, Rustoro>{

	@Autowired
	protected InventorRustoroRepository inventorRustoroRepository;
	
	@Autowired
	protected InventorItemRepository inventorItemRepository;
	
	@Autowired
	protected ChangeCurrencyLibrary changeLibrary;
	
	@Override
	public boolean authorise(final Request<Rustoro> request) {
		assert request != null;
		
		boolean result;
		
		final int rustoroId;
		final Rustoro rustoro;
		final Inventor inventor;
		
		rustoroId = request.getModel().getInteger("id");
		rustoro = this.inventorRustoroRepository.findRustoroById(rustoroId);
		
		inventor = rustoro.getItem().getInventor();
		
		result = request.isPrincipal(inventor);
		
		return result;
	}

	@Override
	public void bind(final Request<Rustoro> request, final Rustoro entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity,errors, "title", "description", "startDate", "finishDate", "budget", "link");
		
		final Model model = request.getModel();
		
		if(model.hasAttribute("defaultCurrency")){
			final Money m = model.getAttribute("defaultCurrency", Money.class);
			entity.setBudget(m);
		}
		
		
	}

	@Override
	public void unbind(final Request<Rustoro> request, final Rustoro entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		final Item i = this.inventorRustoroRepository.findItemByRustoroId(entity.getId());
		
		request.unbind(entity, model, "code", "title", "description", "creationMoment", "startDate", "finishDate", "budget", "link");
		model.setAttribute("itemName", i.getName());
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		if(!(entity.getBudget().getCurrency().equals(defaultCurrency))) {
			
			final Money m = this.changeLibrary.computeMoneyExchange(entity.getBudget(), defaultCurrency).getTarget();
			
			model.setAttribute("showDefaultCurrency", true);
			model.setAttribute("budget",m);
			model.setAttribute("defaultCurrency", entity.getBudget());
		}
		
	}

	@Override
	public Rustoro findOne(final Request<Rustoro> request) {
		assert request != null;
		
		Rustoro result;
		int rustoroId;
		
		rustoroId = request.getModel().getInteger("id");
		result = this.inventorRustoroRepository.findRustoroById(rustoroId);
		
		return result;
	}

	@Override
	public void validate(final Request<Rustoro> request, final Rustoro entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		final List<String> acceptedCurrencies = AcceptedCurrencyLibrary.getAcceptedCurrencies(this.inventorItemRepository.findAcceptedCurrencies());
		
		boolean spamWord;
		final boolean spamWordTitle;
		
		final Configuration configuration = this.inventorItemRepository.configuration();
		final AntiSpam antiSpam = new AntiSpam(configuration.getStrongSpamWords(), configuration.getStrongSpamThreshold(), configuration.getWeakSpamWords(), configuration.getWeakSpamThreshold(), entity.getDescription());
		spamWord = antiSpam.getAvoidSpam();
		errors.state(request, !spamWord, "description", "inventor.rustoro.form.error.spamWord");
		
		final AntiSpam antiSpamTitle = new AntiSpam(configuration.getStrongSpamWords(), configuration.getStrongSpamThreshold(), configuration.getWeakSpamWords(), configuration.getWeakSpamThreshold(), entity.getTitle());
		spamWordTitle = antiSpamTitle.getAvoidSpam();
		errors.state(request, !spamWordTitle, "title", "inventor.rustoro.form.error.spamWord");
		
		if(!(request.getModel().hasAttribute("defaultCurrency"))){
			
			if(!errors.hasErrors("budget")) {
				boolean acceptedCurrency;
				
				acceptedCurrency = acceptedCurrencies.contains(entity.getBudget().getCurrency());
				
				errors.state(request, acceptedCurrency, "budget", "inventor.rustoro.form.error.acceptedCurrency");
				
				boolean positiveValue;
				
				positiveValue = entity.getBudget().getAmount()>0;
				
				errors.state(request, positiveValue, "budget", "inventor.rustoro.form.error.positiveValue");
			}
			
		}else {
			
			if(!errors.hasErrors("defaultCurrency")) {
				boolean acceptedCurrency;
				
				acceptedCurrency = acceptedCurrencies.contains(entity.getBudget().getCurrency());
				
				errors.state(request, acceptedCurrency, "defaultCurrency", "inventor.rustoro.form.error.acceptedCurrency");
				
				boolean positiveValue;
				
				positiveValue = entity.getBudget().getAmount()>0;
				
				errors.state(request, positiveValue, "defaultCurrency", "inventor.rustoro.form.error.positiveValue");
			}
			
		}
		
		if (!errors.hasErrors("startDate")) {
			
			final Calendar calendar = Calendar.getInstance();
			Date minimumPeriodStart;
			
			calendar.setTime(entity.getCreationMoment());
			calendar.add(Calendar.MONTH, 1);
			minimumPeriodStart = calendar.getTime();
			
			
			errors.state(request, entity.getStartDate().after(minimumPeriodStart), "startDate", "inventor.rustoro.form.error.acceptedPeriodTime.start");
			
		}
		
		if (!errors.hasErrors("finishDate")) {
			
			final Calendar calendar = Calendar.getInstance();
			Date minimumPeriodFinish;
			
			calendar.setTime(entity.getStartDate());
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			minimumPeriodFinish = calendar.getTime();
			
			
			errors.state(request, entity.getFinishDate().after(minimumPeriodFinish), "finishDate", "inventor.rustoro.form.error.acceptedPeriodTime.finish");
			
		}
		
	}

	@Override
	public void update(final Request<Rustoro> request, final Rustoro entity) {
		assert request != null;
		assert entity != null;
		
		this.inventorItemRepository.save(entity);
		
	} 
	

}
