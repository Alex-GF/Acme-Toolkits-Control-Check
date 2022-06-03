package acme.features.inventor.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.rustoro.Rustoro;
import acme.entities.item.Item;
import acme.entities.item.ItemType;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractShowService;
import acme.roles.Inventor;
import acme.utils.ChangeCurrencyLibrary;

@Service
public class InventorShowItemService implements AbstractShowService<Inventor,Item> {
	
	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorItemRepository inventorItemRepository;
	
	@Autowired
	protected ChangeCurrencyLibrary changeLibrary;

	// AbstractShowService<Authenticated, Item> interface ---------------------------


	@Override
	public boolean authorise(final Request<Item> request) {
		assert request != null;
		
		boolean result = true;
		
		int itemId;
		Item item;
		Inventor inventor;
		
		itemId = request.getModel().getInteger("id");
		item = this.inventorItemRepository.findItemByItemId(itemId);
		inventor = item.getInventor();
		
		result = request.isPrincipal(inventor);
		
		return result;
	}

	@Override
	public Item findOne(final Request<Item> request) {
		assert request != null;

		Item result;
		final int itemId;

		itemId = request.getModel().getInteger("id");
		result = this.inventorItemRepository.findItemByInventorAndItemId(request.getPrincipal().getAccountId(), itemId);
		
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		if(!(result.getRetailPrice().getCurrency().equals(defaultCurrency))){
			result.setRetailPrice(this.changeLibrary.computeMoneyExchange(result.getRetailPrice(), defaultCurrency).getTarget());
		}

		return result;
	}

	@Override
	public void unbind(final Request<Item> request, final Item entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "name", "technology", "code", "retailPrice", "description", "link", "type", "published");
		
		final String defaultCurrency = this.inventorItemRepository.findDefaultCurrency();
		
		final Item item = this.inventorItemRepository.findItemByItemId(entity.getId());
		
		if(!(item.getRetailPrice().getCurrency().equals(defaultCurrency))) {
			model.setAttribute("showDefaultCurrency", true);
			model.setAttribute("defaultCurrency",item.getRetailPrice());
		}
		
		if(item.getType().equals(ItemType.TOOL)) {
			model.setAttribute("isTool", true);
			
			final Item rustoroItem = this.inventorItemRepository.itemWhithoutRustoro(item.getId());
			
			if(rustoroItem != null) {
				model.setAttribute("showRustoro", true);
				
				final Rustoro rustoro = this.inventorItemRepository.findRustoroByItemId(rustoroItem.getId());
				model.setAttribute("rustoroId", rustoro.getId());
				
			}else {
				model.setAttribute("showRustoro", false);
			}
			
		}
		
	}
}
