package acme.features.inventor.rustoro;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.rustoro.Rustoro;
import acme.framework.controllers.AbstractController;
import acme.roles.Inventor;

@Controller
public class InventorRustoroController extends AbstractController<Inventor, Rustoro> {

	// Internal state ---------------------------------------------------------
	
	@Autowired
	protected InventorListRustoroService	listRustoroService;
	
	@Autowired
	protected InventorShowRustoroService showRustoroService;
	
	@Autowired
	protected InventorRustoroCreateService createRustoroService;
	
	@Autowired
	protected InventorRustoroUpdateService updateRustoroService;
	
	@Autowired
	protected InventorRustoroDeleteService deleteRustoroService;
	
	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.addCommand("list", this.listRustoroService);
		super.addCommand("show", this.showRustoroService);
		super.addCommand("create", this.createRustoroService);
		super.addCommand("update", this.updateRustoroService);
		super.addCommand("delete", this.deleteRustoroService);
	}
}
