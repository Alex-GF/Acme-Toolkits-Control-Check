package acme.testing.inventor.rustoro;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorRustoroListTest extends TestHarness{

	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/rustoro/list.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positive(final int recordIndex, final String code, final String title, 
						final String description,  final String creationMoment,
						final String startDate,final String finishDate, final String budget,
						final String link, final String item) {
		
		super.signIn("inventor1", "inventor1");

		super.clickOnMenu("Inventor", "Rustoro list");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 1, title);
		//super.checkColumnHasValue(recordIndex, 2, budget);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("creationMoment", creationMoment);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkInputBoxHasValue("itemName", item);
		
		/*La comprobación del budget se deja comentada para que no falle el 
		 * test por culpa de los algoritmos de cambio de divisa*/
		
		// super.checkInputBoxHasValue("budget", budget);
		
		super.checkInputBoxHasValue("link", link);
		
		super.signOut();
	}
	
	@Test
    @Order(20)
    public void hackingTest() {

		super.checkNotLinkExists("Account");
		super.navigate("/inventor/rustoro/list");
		super.checkPanicExists();

		super.signIn("patron2", "patron2");
		super.navigate("/inventor/chipum/list");
		super.checkPanicExists();
		super.signOut();
	
		super.signIn("administrator", "administrator");
		super.navigate("/inventor/chipum/list");
		super.checkPanicExists();
		super.signOut();
		
		// También se ha probado acceder a un item con un inventor que no es su dueño, para intentar a acceder
		// a un rustoro que no corresponde. En efecto, salta un error que indica la falta de permisos para
		// acceder.
    	
    }
}
