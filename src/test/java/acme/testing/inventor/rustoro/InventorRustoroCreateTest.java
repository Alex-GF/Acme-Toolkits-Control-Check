package acme.testing.inventor.rustoro;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorRustoroCreateTest extends TestHarness {

    @ParameterizedTest
    @CsvFileSource(resources = "/inventor/rustoro/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(10)
    public void positive(final int recordIndex, final String title, final String description, final String startDate, final String finishDate, final String budget, final String link, final String item, final int itemIndex) {
        super.signIn("inventor1", "inventor1");

        super.clickOnMenu("Inventor", "My Items");
        super.checkListingExists();
        super.sortListing(0, "asc");
        
        super.clickOnListingRecord(itemIndex);
        
		super.clickOnButton("Add rustoro");

        final LocalDateTime now = LocalDateTime.now();
        final String date = now.getYear() + "/"
                + (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue()) + "/"
                + (now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : now.getDayOfMonth()) +" "
                + (now.getHour() < 10 ? "0" + now.getHour() : now.getHour()) + ":"
                + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute());
        final String code = ""+ String.valueOf(now.getYear()).substring(2)
            			+ (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue()) 
            			+ (now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : now.getDayOfMonth())+ "-ABC";
        
        
        super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        super.fillInputBoxIn("startDate", startDate);
        super.fillInputBoxIn("finishDate", finishDate);
        super.fillInputBoxIn("budget", budget);
        super.fillInputBoxIn("link", link);
        super.clickOnSubmit("Create");

        super.clickOnMenu("Inventor", "Rustoro list");
        super.checkListingExists();
        super.sortListing(0, "asc");
        super.checkColumnHasValue(recordIndex, 1, title);
        super.checkColumnHasValue(recordIndex, 2, budget);
        super.checkColumnHasValue(recordIndex, 3, item);

        super.clickOnListingRecord(recordIndex);

        super.checkFormExists();
        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        super.checkInputBoxHasValue("creationMoment", date);
        super.fillInputBoxIn("startDate", startDate);
        super.fillInputBoxIn("finishDate", finishDate);
        super.fillInputBoxIn("budget", budget);
        super.fillInputBoxIn("link", link);

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/inventor/rustoro/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(20)
    public void negative(final int recordIndex,final String fakeCode, final String title, final String description, final String startDate, final String finishDate, final String budget, final String link, final int itemIndex) {
        super.signIn("inventor1", "inventor1");

        super.clickOnMenu("Inventor", "My Items");
        super.checkListingExists();
        super.sortListing(0, "asc");
        
        super.clickOnListingRecord(itemIndex);
        
		super.clickOnButton("Add rustoro");
		
		String code = fakeCode;
		
		if (fakeCode.equals("..")) {
			final LocalDateTime now = LocalDateTime.now();
			code = ""+ String.valueOf(now.getYear()).substring(2)
	 			+ (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue()) 
	 			+ (now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : now.getDayOfMonth())+ "-ABC";
			
		}
        
		
		super.fillInputBoxIn("code", code);
        super.fillInputBoxIn("title", title);
        super.fillInputBoxIn("description", description);
        super.fillInputBoxIn("startDate", startDate);
        super.fillInputBoxIn("finishDate", finishDate);
        super.fillInputBoxIn("budget", budget);
        super.fillInputBoxIn("link", link);
        super.clickOnSubmit("Create");

        super.checkErrorsExist();
        super.signOut();
    }

    @Test
    @Order(30)
    public void hackingTest() {

        //Se ha comprobado a intentar entrar en el formulario de crear Rustoro
    	//desde usuarios tipo Any, Patron y Administrator.
    	//También se ha intentado entrar en el formulario de crear Rustoro
    	//desde un inventor que haya creado un item con un Rustoro asociado.
    	
    }
}
