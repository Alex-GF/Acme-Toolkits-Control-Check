package acme.features.inventor.rustoro;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.rustoro.Rustoro;
import acme.entities.item.Item;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Inventor;

@Repository
public interface InventorRustoroRepository extends AbstractRepository{
	
	
	@Query("SELECT c FROM Rustoro c WHERE c.item.inventor.userAccount.id = :inventorId")
	Collection<Rustoro> findAllMine(int inventorId);
	
	
	@Query("SELECT c FROM Rustoro c WHERE c.id = :rustoroId")
	Rustoro findRustoroById(int rustoroId);
	
	
	@Query("SELECT c.item FROM Rustoro c WHERE c.id = :rustoroId")
	Item findItemByRustoroId(int rustoroId);
	
	@Query("SELECT c.code FROM Rustoro c")
	List<String> findAllCodes();
	
	@Query("SELECT i FROM Item i WHERE i.id = :itemId")
	Item findItemByItemId(int itemId);
	
	@Query("SELECT c FROM Rustoro c WHERE c.item.id = :itemId")
	Rustoro findRustoroByItemId(int itemId);
	
	@Query("SELECT i FROM Inventor i WHERE i.userAccount.id = :inventorId")
	Inventor findInventorByInventorId(int inventorId);
}