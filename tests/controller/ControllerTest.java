package controller;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import integration.dbhandler.SystemCreator;
import integration.dbhandler.data.ItemDescription;
import model.dto.RecentPurchaseInformation;
import model.dto.PriceInformation;
import model.util.Amount;
import model.util.IdentificationNumber;

class ControllerTest {
	private Controller controller;
	private ItemDescription existingDescriptionApple;
	private ItemDescription existingDescriptionCoffee;

	private Amount itemPrice;
	private Amount itemVatTax;
	private double stdVatRate = 0.16;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		SystemCreator creator = new SystemCreator();
		controller = new Controller(creator);

		existingDescriptionApple = creator.getInventorySystem()
				.retrieveItemDescription(new IdentificationNumber(123));
		existingDescriptionCoffee = creator.getInventorySystem()
				.retrieveItemDescription(new IdentificationNumber(666));

		itemVatTax = new Amount(stdVatRate * 5);
		itemPrice = new Amount(5).add(itemVatTax);
	}

	@AfterEach
	void tearDown() throws Exception {
		controller = null;
		existingDescriptionApple = null;
	}

	@Test
	void testCorrectCurrentSaleInfo() {
		controller.startSale();
		RecentPurchaseInformation saleInfo = controller.processItem(existingDescriptionApple.getID(), 1);

		Amount expectedRunningTotal = itemPrice;
		Amount retrievedRunningTotal = saleInfo.getRunningTotal().getTotalPrice();
		assertTrue(retrievedRunningTotal.equals(expectedRunningTotal),
				"Running total does not match expected price. Got " + retrievedRunningTotal + " expected "
						+ expectedRunningTotal);

		ItemDescription expectedItemDescription = existingDescriptionApple;
		ItemDescription retrievedItemDescription = saleInfo.getLatestItemInformation().getItemDescription();
		assertTrue(retrievedItemDescription.equals(expectedItemDescription),
				"Retrieved item description does not match most recently processed item.");

	}

	@Test
	void testCorrectPriceInfo() {
		int purchasedQuantity = 3;
		controller.startSale();
		controller.processItem(existingDescriptionApple.getID(), purchasedQuantity);

		Amount expectedTotalPrice = itemPrice.multiply(purchasedQuantity);
		Amount expectedTotalVat = itemVatTax.multiply(purchasedQuantity);

		PriceInformation priceInfo = controller.endSale();
		Amount receivedTotalPrice = priceInfo.getTotalPrice();
		Amount receivedTotalVat = priceInfo.getTotalVat();

		assertTrue(receivedTotalPrice.equals(expectedTotalPrice),
				"Received total price " + receivedTotalPrice.getValue().doubleValue() + " does not match, expected "
						+ expectedTotalPrice.getValue());
		assertTrue(receivedTotalVat.equals(expectedTotalVat),
				"Received total VAT " + receivedTotalVat.getValue().doubleValue() + " does not match, expected "
						+ expectedTotalVat.getValue());
	}

	@Test
	void testCorrectChangeAmount() {
		controller.startSale();
		controller.processItem(existingDescriptionApple.getID(), 1);
		controller.processItem(existingDescriptionCoffee.getID(), 4);
		
		Amount totalPrice = controller.endSale().getTotalPrice();
		Amount amountPaid = new Amount(232.25);
		
		Amount expectedChangeAmount = amountPaid.subtract(totalPrice);
		Amount actualChangeAmount = controller.processSale(amountPaid);
		
		assertEquals(expectedChangeAmount, actualChangeAmount, "Calculated change is incorrect. Expected " + expectedChangeAmount + ", got " + actualChangeAmount);
	}

}
