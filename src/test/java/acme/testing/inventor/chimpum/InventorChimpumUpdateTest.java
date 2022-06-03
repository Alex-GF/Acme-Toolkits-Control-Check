package acme.testing.inventor.chimpum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.core.annotation.Order;

import acme.testing.TestHarness;

public class InventorChimpumUpdateTest extends TestHarness{
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/chimpum/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveTest(final int recordIndex, final String title, final String description, final String startDate, final String finishDate,
		final String budget, final String link) {
		
		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "My Items");
		super.checkListingExists();
		
		super.sortListing(1, "asc");
		
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		
		super.clickOnButton("Show chimpum");
		
		super.checkFormExists();
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("budget", budget);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");
		
		super.clickOnButton("Show chimpum");
		
		super.checkFormExists();
		
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("description", description);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkInputBoxHasValue("budget", budget);
		super.checkInputBoxHasValue("link", link);
		
		
		super.signOut();
		
		
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/chimpum/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void negativeTest(final int recordIndex, final String title, final String description, final String startDate, final String finishDate,
		final String budget, final String link) {
		
		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "My Items");
		super.checkListingExists();
		
		super.sortListing(1, "asc");
		
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		
		super.clickOnButton("Show chimpum");
		
		super.checkFormExists();
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("budget", budget);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");
		
		super.checkErrorsExist();
		
		
		super.signOut();
		
		
	}
	
	@Test
	@Order(30)
	public void hackingTest() {
		
		super.checkNotLinkExists("Account");
		super.navigate("/inventor/chimpum/update");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.navigate("/inventor/chimpum/update");
		super.checkPanicExists();
		super.signOut();

		super.signIn("patron2", "patron2");
		super.navigate("/inventor/chimpum/update");
		super.checkPanicExists();
		super.signOut();
		
		// SUGERENCIA: el framework no proporciona suficiente soporte para implementar este caso de hacking,
		// SUGERENCIA+ por lo que debe realizarse manualmente:
		// SUGERENCIA+ a) No actualizar un chimpum de un item con un inventor que no sea el propietario de ese item 
		
		
	}

	
	

}
