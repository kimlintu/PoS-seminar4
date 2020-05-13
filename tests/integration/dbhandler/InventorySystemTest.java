package integration.dbhandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import integration.dbhandler.data.ItemDescription;
import model.dto.Receipt;
import model.pos.Sale;
import model.util.Amount;
import model.util.IdentificationNumber;

class InventorySystemTest {
	private InventorySystem inventory;
	private IdentificationNumber existingItemID;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		inventory = new InventorySystem();
		existingItemID = new IdentificationNumber(123);
	}

	@AfterEach
	void tearDown() throws Exception {
		inventory = null;
		existingItemID = null;
	}

	@Test
	void testFindExistingItem() throws InvalidItemIDException {
		ItemDescription description = inventory.retrieveItemDescription(existingItemID);

		assertTrue(description.getID().equals(existingItemID), "ID " + existingItemID + " "
				+ "does not match entry description retrieved from database with ID " + description.getID() + ".");
	}

	@Test
	void testUpdateQuantityOfStoredItem() throws InvalidItemIDException {
		Sale sale = new Sale();
		int quantityBeforeUpdate = inventory.getAvailableQuantityOfItem(existingItemID);

		sale.addItemToSale(inventory.retrieveItemDescription(existingItemID), 12);
		Receipt saleInfo = sale.processSale(new Amount(100), new Amount(100));
		
		int expectedQuantityAfterUpdate = quantityBeforeUpdate - 12;

		inventory.updateQuantityOfItems(saleInfo);
		int quantityAfterUpdate = inventory.getAvailableQuantityOfItem(existingItemID);

		assertEquals(expectedQuantityAfterUpdate, quantityAfterUpdate,
				"Quantity in inventory stock has not been updated for purchased item. Expected "
						+ expectedQuantityAfterUpdate + " but got " + quantityAfterUpdate);
	}
	
	@Test
	void testUseInvalidID() {
		IdentificationNumber invalidID = new IdentificationNumber(981237);
		
		try {
			ItemDescription description = inventory.retrieveItemDescription(invalidID);
			fail("Description got retrieved using invalid item ID. No exception was thrown.");
		} catch (InvalidItemIDException e) {
			assertTrue(e.getMessage().contains(invalidID.toString()), "Exception error message does not contain the invalid ID.");
		}
	}
	
	@Test
	void testNoExceptionWithValidID() {
		try {
			ItemDescription description = inventory.retrieveItemDescription(existingItemID);
		} catch(InvalidItemIDException e) {
			fail("Exception was thrown when using valid ID.");
		}
	}
	
	@Test
	void testDBException() throws InvalidItemIDException {
		try {
			ItemDescription description = inventory.retrieveItemDescription(new IdentificationNumber(987987));
			fail("No exception was thrown with unconnected system.");
		} catch (InventoryException e) {
			
		}
	}
}
