package acme.features.inventor.rustoro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.item.Item;
import acme.entities.rustoro.Rustoro;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;
import acme.utils.ChangeCurrencyLibrary;

@Service
public class InventorShowRustoroService implements AbstractShowService<Inventor,Rustoro> {
	
	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorRustoroRepository inventorRustoroRepository;
	
	@Autowired
	protected InventorItemRepository inventorItemRepository;
	
	@Autowired
	protected ChangeCurrencyLibrary changeLibrary;

	// AbstractShowService<Authenticated, Item> interface ---------------------------


	@Override
	public boolean authorise(final Request<Rustoro> request) {
		assert request != null;
		
		boolean result;
		
		result = request.getPrincipal().hasRole(Inventor.class);
		
		return result;
	}

	@Override
	public Rustoro findOne(final Request<Rustoro> request) {
		assert request != null;

		Rustoro result;
		final int rustoroId;

		rustoroId = request.getModel().getInteger("id");
		
		//itemId = request.getModel().getInteger("itemId");
		result = this.inventorRustoroRepository.findRustoroById(rustoroId);
		
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		if(!(result.getQuota().getCurrency().equals(defaultCurrency))){
			result.setQuota(this.changeLibrary.computeMoneyExchange(result.getQuota(), defaultCurrency).getTarget());
		}

		return result;
	}

	@Override
	public void unbind(final Request<Rustoro> request, final Rustoro entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		final Item i = this.inventorRustoroRepository.findItemByRustoroId(entity.getId());
		
		request.unbind(entity, model, "code", "name", "explanation", "creationMoment", "startDate", "finishDate", "quota", "moreInfo");
		model.setAttribute("itemName", i.getName());
		
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		final Rustoro rustoro = this.inventorRustoroRepository.findRustoroById(entity.getId());
		
		if(!(rustoro.getQuota().getCurrency().equals(defaultCurrency))) {
			model.setAttribute("showDefaultCurrency", true);
			model.setAttribute("defaultCurrency",rustoro.getQuota());
		}
		
	}
}
