package acme.features.inventor.rustoro;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.rustoro.Rustoro;
import acme.entities.item.Item;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractListService;
import acme.roles.Inventor;
import acme.utils.ChangeCurrencyLibrary;

@Service
public class InventorListRustoroService implements AbstractListService<Inventor, Rustoro>{

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorRustoroRepository inventorRustoroRepository;
	
	@Autowired
	protected InventorItemRepository invetorItemRepository;
	
	@Autowired
	protected ChangeCurrencyLibrary changeLibrary;

	// AbstractListService<Inventor, Quantity> interface ---------------------------


	@Override
	public boolean authorise(final Request<Rustoro> request) {
		assert request != null;

		boolean result;

		result = request.getPrincipal().hasRole(Inventor.class);

		return result;
	}

	@Override
	public void unbind(final Request<Rustoro> request, final Rustoro entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		final Item i = this.inventorRustoroRepository.findItemByRustoroId(entity.getId());
		
		request.unbind(entity, model, "code", "title", "budget");
		model.setAttribute("itemName", i.getName());
		
	}

	@Override
	public Collection<Rustoro> findMany(final Request<Rustoro> request) {
		assert request != null;

		final Collection<Rustoro> result;

		result = this.inventorRustoroRepository.findAllMine(request.getPrincipal().getAccountId());
		
		final String defaultCurrency = this.invetorItemRepository.findDefaultCurrency();
		
		result.stream()
			.filter(c->!(c.getBudget().getCurrency().equals(defaultCurrency)))
			.forEach(c->c.setBudget(this.changeLibrary.computeMoneyExchange(c.getBudget(), defaultCurrency).getTarget()));
		
		return result;
	}
}
