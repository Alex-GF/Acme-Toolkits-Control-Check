package acme.testing.inventor.rustoro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.core.annotation.Order;

import acme.testing.TestHarness;

public class InventorRustoroUpdateTest extends TestHarness{
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/rustoro/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveTest(final int recordIndex, final String name, final String explanation, 
							final String startDate, final String finishDate,
							final String quota, final String moreInfo) {
		
		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "My Items");
		super.checkListingExists();
		
		super.sortListing(1, "asc");
		
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		
		super.clickOnButton("Show rustoro");
		
		super.checkFormExists();
		
		super.fillInputBoxIn("name", name);
		super.fillInputBoxIn("explanation", explanation);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("quota", quota);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.clickOnSubmit("Update");
		
		super.clickOnButton("Show rustoro");
		
		super.checkFormExists();
		
		super.checkInputBoxHasValue("name", name);
		super.checkInputBoxHasValue("explanation", explanation);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkInputBoxHasValue("quota", quota);
		super.checkInputBoxHasValue("moreInfo", moreInfo);
		
		
		super.signOut();
		
		
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/rustoro/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(20)
	public void negativeTest(final int recordIndex, final String name, final String explanation, 
							final String startDate, final String finishDate,
							final String quota, final String moreInfo) {
		
		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "My Items");
		super.checkListingExists();
		
		super.sortListing(1, "asc");
		
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		
		super.clickOnButton("Show rustoro");
		
		super.checkFormExists();
		
		super.fillInputBoxIn("name", name);
		super.fillInputBoxIn("explanation", explanation);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("quota", quota);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.clickOnSubmit("Update");
		
		super.checkErrorsExist();
		
		
		super.signOut();
		
		
	}
	
	@Test
	@Order(30)
	public void hackingTest() {
		
		super.checkNotLinkExists("Account");
		super.navigate("/inventor/rustoro/update");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.navigate("/inventor/rustoro/update");
		super.checkPanicExists();
		super.signOut();

		super.signIn("patron2", "patron2");
		super.navigate("/inventor/rustoro/update");
		super.checkPanicExists();
		super.signOut();
		
		// SUGERENCIA: el framework no proporciona suficiente soporte para implementar este caso de hacking,
		// SUGERENCIA+ por lo que debe realizarse manualmente:
		// SUGERENCIA+ a) No actualizar un rustoro de un item con un inventor que no sea el propietario de ese item 
		
		
	}

	
	

}
