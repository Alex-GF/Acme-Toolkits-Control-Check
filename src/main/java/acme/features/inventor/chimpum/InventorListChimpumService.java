package acme.features.inventor.chimpum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.entities.item.Item;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractListService;
import acme.roles.Inventor;
import acme.utils.ChangeCurrencyLibrary;

@Service
public class InventorListChimpumService implements AbstractListService<Inventor, Chimpum>{

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorChimpumRepository inventorChimpumRepository;
	
	@Autowired
	protected InventorItemRepository invetorItemRepository;
	
	@Autowired
	protected ChangeCurrencyLibrary changeLibrary;

	// AbstractListService<Inventor, Quantity> interface ---------------------------


	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;

		boolean result;

		result = request.getPrincipal().hasRole(Inventor.class);

		return result;
	}

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		final Item i = this.inventorChimpumRepository.findItemByChimpumId(entity.getId());
		
		request.unbind(entity, model, "code", "title", "budget");
		model.setAttribute("itemName", i.getName());
		
	}

	@Override
	public Collection<Chimpum> findMany(final Request<Chimpum> request) {
		assert request != null;

		final Collection<Chimpum> result;

		result = this.inventorChimpumRepository.findAllMine(request.getPrincipal().getAccountId());
		
		final String defaultCurrency = this.invetorItemRepository.findDefaultCurrency();
		
		result.stream()
			.filter(c->!(c.getBudget().getCurrency().equals(defaultCurrency)))
			.forEach(c->c.setBudget(this.changeLibrary.computeMoneyExchange(c.getBudget(), defaultCurrency).getTarget()));
		
		return result;
	}
}
