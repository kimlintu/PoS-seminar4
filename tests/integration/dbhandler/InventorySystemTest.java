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

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		inventory = new InventorySystem();
	}

	@AfterEach
	void tearDown() throws Exception {
		inventory = null;
	}

	@Test
	void testFindExistingItem() {
		IdentificationNumber existingID = new IdentificationNumber(123);
		ItemDescription description = inventory.retrieveItemDescription(existingID);

		assertTrue(description.getID().equals(existingID), "ID " + existingID + " "
				+ "does not match entry description retrieved from database with ID " + description.getID() + ".");
	}

	@Test
	void testFindItemUsingInvalidID() {
		IdentificationNumber invalidID = new IdentificationNumber(981237);
		ItemDescription itemDescription = inventory.retrieveItemDescription(invalidID);

		if (itemDescription != null) {
			fail("Item " + itemDescription + " was retrieved using nonexisting ID " + invalidID + ", expected null. ");
		}
	}

	@Test
	void testUpdateQuantityOfStoredItem() {
		Sale sale = new Sale();
		IdentificationNumber existingItemID = new IdentificationNumber(123);
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

}
