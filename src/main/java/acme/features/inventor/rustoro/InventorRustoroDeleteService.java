package acme.features.inventor.rustoro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.rustoro.Rustoro;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Inventor;

@Service
public class InventorRustoroDeleteService implements AbstractDeleteService<Inventor, Rustoro>{
	
	@Autowired
	protected InventorRustoroRepository inventorRustoroRepository;
	
	@Autowired
	protected InventorItemRepository inventorItemRepository;

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
		
		request.bind(entity, errors, "code", "name", "explanation", "creationMoment", "startDate", "finishDate", "quota", "moreInfo");
	}

	@Override
	public void unbind(final Request<Rustoro> request, final Rustoro entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "name", "explanation", "creationMoment", "startDate", "finishDate", "quota", "moreInfo");
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
		
		
	}

	@Override
	public void delete(final Request<Rustoro> request, final Rustoro entity) {
		assert request != null;
		assert entity != null;
		
		this.inventorRustoroRepository.delete(entity);
		
	}
	
	
	

}
