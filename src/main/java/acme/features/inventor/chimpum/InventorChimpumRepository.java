package acme.features.inventor.chimpum;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.chimpum.Chimpum;
import acme.entities.item.Item;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Inventor;

@Repository
public interface InventorChimpumRepository extends AbstractRepository{
	
	
	@Query("SELECT c FROM Chimpum c WHERE c.item.inventor.userAccount.id = :inventorId")
	Collection<Chimpum> findAllMine(int inventorId);
	
	
	@Query("SELECT c FROM Chimpum c WHERE c.id = :chimpumId")
	Chimpum findChimpumById(int chimpumId);
	
	
	@Query("SELECT c.item FROM Chimpum c WHERE c.id = :chimpumId")
	Item findItemByChimpumId(int chimpumId);
	
	@Query("SELECT c.code FROM Chimpum c")
	List<String> findAllCodes();
	
	@Query("SELECT i FROM Item i WHERE i.id = :itemId")
	Item findItemByItemId(int itemId);
	
	@Query("SELECT c FROM Chimpum c WHERE c.item.id = :itemId")
	Chimpum findChimpumByItemId(int itemId);
	
	@Query("SELECT i FROM Inventor i WHERE i.userAccount.id = :inventorId")
	Inventor findInventorByInventorId(int inventorId);
}