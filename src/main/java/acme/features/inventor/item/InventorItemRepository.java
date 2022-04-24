package acme.features.inventor.item;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.item.Item;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface InventorItemRepository extends AbstractRepository{
	
	@Query("SELECT i FROM Item i WHERE i.inventor.userAccount.id = :inventorId")
	Collection<Item> findItemsByInventorId(int inventorId);
	
	@Query("SELECT i FROM Item i WHERE i.inventor.userAccount.id = :inventorId AND i.id = :itemId")
	Item findItemById(int inventorId, int itemId);
	
	@Query("SELECT c.defaultCurrency FROM Configuration c")
	String findDefaultCurrency();
}