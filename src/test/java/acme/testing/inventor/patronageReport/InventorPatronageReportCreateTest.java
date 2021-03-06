package acme.testing.inventor.patronageReport;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorPatronageReportCreateTest extends TestHarness {

    @ParameterizedTest
    @CsvFileSource(resources = "/inventor/patronageReport/positive.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(30)
    public void positive(final int recordIndex, final String memorandum, final String link) {
        super.signIn("inventor1", "inventor1");

        super.clickOnMenu("Inventor", "Patronage report list");
		super.clickOnButton("Create");

        final LocalDateTime now = LocalDateTime.now();
        final String date = now.getYear() + "/"
                + (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue()) + "/"
                + now.getDayOfMonth() + " " + now.getHour() + ":"
                + (now.getMinute() < 10 ? "0" + now.getMinute() : now.getMinute());
        super.fillInputBoxIn("memorandum", memorandum);
        super.fillInputBoxIn("link", link);
        super.fillInputBoxIn("confirmation", "true");
        super.clickOnSubmit("Create");

        super.clickOnMenu("Inventor", "Patronage report list");
        super.checkListingExists();
        super.sortListing(0, "asc");
        super.checkColumnHasValue(recordIndex+1, 0, date);
        super.checkColumnHasValue(recordIndex+1, 1, memorandum);
        

        super.clickOnListingRecord(recordIndex+1);

        super.checkFormExists();
        super.checkInputBoxHasValue("memorandum", memorandum);
        super.checkInputBoxHasValue("link", link);

        super.signOut();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/inventor/patronageReport/negative.csv", encoding = "utf-8", numLinesToSkip = 1)
    @Order(20)
    public void negative(final String recordIndex, final String memorandum, final String link) {
        super.signIn("inventor1", "inventor1");

        super.clickOnMenu("Inventor", "Patronage report list");
        super.clickOnButton("Create");
        super.fillInputBoxIn("memorandum", memorandum);
        super.fillInputBoxIn("link", link);
        super.fillInputBoxIn("confirmation", "true");
        super.clickOnSubmit("Create");

        super.checkErrorsExist();
        super.signOut();
    }

    @Test
    @Order(10)
    public void hackingTest() {


        super.checkNotLinkExists("Account");
        super.navigate("/inventor/patronage-report/create");
        super.checkPanicExists();

        super.signIn("patron2", "patron2");
        super.navigate("/inventor/patronage-report/create");
        super.checkPanicExists();
        super.signOut();
        
        super.signIn("administrator", "administrator");
        super.navigate("/inventor/patronage-report/create");
        super.checkPanicExists();
        super.signOut();
    }

}