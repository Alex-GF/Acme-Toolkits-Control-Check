/*
 * EmployerJobPublishTest.java
 *
 * Copyright (C) 2012-2022 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.inventor.rustoro;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import acme.testing.TestHarness;

public class InventorRustoroDeleteTest extends TestHarness {

	private final Integer RECORD_INDEX_RUSTORO_PRUEBA = 0;
	private final Integer RECORD_INDEX_ITEM_PRUEBA = 4;
	
	// Lifecycle management ---------------------------------------------------
	
	@BeforeAll
	public void createToolkitBeforeAllTests() {
		super.signIn("inventor1", "inventor1");

		super.clickOnMenu("Inventor", "My Items");
		super.checkListingExists();
		super.checkColumnHasValue(this.RECORD_INDEX_ITEM_PRUEBA, 0, "Taladro");
		super.clickOnListingRecord(this.RECORD_INDEX_ITEM_PRUEBA);
		super.checkFormExists();
		
		final LocalDateTime now = LocalDateTime.now();
		final String code = ""+ String.valueOf(now.getYear()).substring(2)
			+ (now.getMonthValue() < 10 ? "0" + now.getMonthValue() : now.getMonthValue()) 
			+ (now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : now.getDayOfMonth())+ ":ABC_32";


		super.clickOnButton("Add rustoro");
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("name", "AA DELETE TEST RUSTORO");
		super.fillInputBoxIn("explanation", "Descripción del delete test");
		
		
		final LocalDateTime start = now.plusMonths(2);
		final LocalDateTime finish = start.plusWeeks(2);
        final String startDate = InventorRustoroDeleteTest.stringDate(start);
        final String finishDate = InventorRustoroDeleteTest.stringDate(finish);
        
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("quota", "EUR 100.00");
		super.fillInputBoxIn("moreInfo", "http:/linkdeprueba.com");
		super.clickOnSubmit("Create");
		super.checkNotErrorsExist();
		
		super.signOut();
	}
	
	// Test cases -------------------------------------------------------------

	@Test
	@Order(10)
	public void positiveTest() {
		super.signIn("inventor1", "inventor1");

		super.clickOnMenu("Inventor", "Rustoro list");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(this.RECORD_INDEX_RUSTORO_PRUEBA, 1, "AA DELETE TEST RUSTORO");

		super.clickOnListingRecord(this.RECORD_INDEX_RUSTORO_PRUEBA);
		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	@Order(30)
	public void hackingTest() {
		// SUGERENCIA: el framework no proporciona suficiente soporte para implementar este caso de hacking,
		// SUGERENCIA+ por lo que debe realizarse manualmente:
		// SUGERENCIA+ a) No se puede borrar un rustoro con un inventor que no sea su propietario.
		
	}

	// Auxiliar methods ------------------------------------------------------

	public static String stringDate(final LocalDateTime date) {
		return date.getYear() + "/" + 
			(date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue()) +
			"/" + date.getDayOfMonth() + " " + date.getHour() + ":" + 
			(date.getMinute() < 10 ? "0" + date.getMinute() : date.getMinute());
	}
	
}