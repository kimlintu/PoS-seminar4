package model.pos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import integration.dbhandler.InvalidItemIDException;
import integration.dbhandler.SystemCreator;
import integration.dbhandler.data.ItemDescription;
import integration.dbhandler.discount.Discount;
import integration.dbhandler.discount.QuantityDiscount;
import model.dto.PurchasedItemInformation;
import model.util.Amount;
import model.util.IdentificationNumber;

class ItemTest {
	private Item item;
	private ItemDescription description;
	private SystemCreator creator;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		creator = SystemCreator.getCreator();
		description = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(123));
		item = new Item(description, 1);
		
	}

	@AfterEach
	void tearDown() throws Exception {
		creator = null;
		description = null;
		item = null;
	}
	
	@Test
	void testCorrectCalculatedPrice() {
		double calculatedPrice = 5 + (0.16 * 5);
		Amount finalPrice = new Amount(calculatedPrice);

		PurchasedItemInformation itemInfo = item.getItemInformation();
		assertTrue(itemInfo.getAccumulatedPrice().equals(finalPrice),
				"Calculated price " + itemInfo.getAccumulatedPrice() + " is not correct. Should be " + calculatedPrice);

	}
	
	@Test
	void applyQuantityDiscount() {
		int quantity = 5;
		Item item = new Item(description, quantity);
		Amount originalUnitPrice = item.getUnitPrice();
		
		Discount discount = new QuantityDiscount((short) 3, new Amount(0.2));
		Amount discountPrice = (originalUnitPrice.multiply(discount.getRate())).multiply(quantity); 
		Amount expectedDiscountedPrice = (originalUnitPrice.multiply(quantity)).subtract(discountPrice);
		
		item.applyDiscount(discount);
		Amount actualDiscountedPrice = item.getUnitPrice().multiply(quantity);
		assertEquals(expectedDiscountedPrice, actualDiscountedPrice, "Actual discounted price does not match expected price.");
	}
	
	@Test
	void testEqual() {
		Item identicalItem = new Item(description, 1);

		assertTrue(item.equals(identicalItem), "Two identical items " + item.toString() + ", " + identicalItem.toString() + " do not equal.");
	}

	@Test
	void testNotEqual() throws InvalidItemIDException {
		ItemDescription differentDescription = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(666));
		Item differentItem = new Item(differentDescription, 1);

		assertFalse(item.equals(differentItem), "Two different items " + item.toString() + ", " + differentItem.toString() + " are equal.");
	}

}
