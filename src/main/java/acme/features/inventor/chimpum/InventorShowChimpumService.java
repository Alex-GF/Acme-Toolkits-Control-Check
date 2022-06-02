package acme.features.inventor.chimpum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.entities.item.Item;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;
import acme.utils.ChangeCurrencyLibrary;

@Service
public class InventorShowChimpumService implements AbstractShowService<Inventor,Chimpum> {
	
	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorChimpumRepository inventorChimpumRepository;
	
	@Autowired
	protected InventorItemRepository inventorItemRepository;
	
	@Autowired
	protected ChangeCurrencyLibrary changeLibrary;

	// AbstractShowService<Authenticated, Item> interface ---------------------------


	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;
		
		boolean result;
		
		result = request.getPrincipal().hasRole(Inventor.class);
		
		return result;
	}

	@Override
	public Chimpum findOne(final Request<Chimpum> request) {
		assert request != null;

		Chimpum result;
		final int chimpumId;

		chimpumId = request.getModel().getInteger("id");
		
		//itemId = request.getModel().getInteger("itemId");
		result = this.inventorChimpumRepository.findChimpumById(chimpumId);
		
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		if(!(result.getBudget().getCurrency().equals(defaultCurrency))){
			result.setBudget(this.changeLibrary.computeMoneyExchange(result.getBudget(), defaultCurrency).getTarget());
		}

		return result;
	}

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		final Item i = this.inventorChimpumRepository.findItemByChimpumId(entity.getId());
		
		request.unbind(entity, model, "code", "title", "description", "creationMoment", "startDate", "finishDate", "budget", "link");
		model.setAttribute("itemName", i.getName());
		
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		final Chimpum chimpum = this.inventorChimpumRepository.findChimpumById(entity.getId());
		
		if(!(chimpum.getBudget().getCurrency().equals(defaultCurrency))) {
			model.setAttribute("showDefaultCurrency", true);
			model.setAttribute("defaultCurrency",chimpum.getBudget());
		}
		
	}
}
