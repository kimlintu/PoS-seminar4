package model.pos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import integration.dbhandler.SystemCreator;
import integration.dbhandler.data.ItemDescription;
import model.dto.PriceInformation;
import model.dto.PurchasedItemInformation;
import model.dto.Receipt;
import model.util.Amount;
import model.util.IdentificationNumber;

class SaleTest {
	private SystemCreator creator;

	private Sale sale;
	
	private Amount stdVatRate;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		creator = new SystemCreator();
		sale = new Sale();
		
		stdVatRate = new Amount(0.16);
	}

	@AfterEach
	void tearDown() throws Exception {
		stdVatRate = null;
		
		sale = null;
		creator = null;
	}
	
	@Test
	void testAddNoItemsToSale() {
		PriceInformation priceInfo = sale.getPriceInformation();
		Amount totalPrice = priceInfo.getTotalPrice();
		Amount totalVat = priceInfo.getTotalVat();
		
		Amount expectedPrice = new Amount(0);
		assertTrue(totalPrice.equals(expectedPrice), "Total price is greater than 0 when no item is added.");
		
		Amount expectedVat = new Amount(0);
		assertTrue(totalVat.equals(expectedVat), "Total VAT tax is greater than 0 when no item is added.");
	}

	@Test
	void testAddOneItemToSale() {
		Amount itemPrice = new Amount(5);
		Amount itemVatRate = stdVatRate;
		Amount vatTax = itemPrice.multiply(itemVatRate);
		int purchasedQuantity = 1;

		ItemDescription newItem = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(123));

		sale.addItemToSale(newItem, purchasedQuantity);

		PriceInformation priceInfo = sale.getPriceInformation();
		Amount totalPrice = priceInfo.getTotalPrice();
		Amount totalVat = priceInfo.getTotalVat();
		
		Receipt saleInfo = sale.processSale(new Amount(100), new Amount(100));
		List<PurchasedItemInformation> itemList = saleInfo.getListOfSoldItems();
		
		PurchasedItemInformation expectedItem = new Item(newItem, purchasedQuantity).getItemInformation();
		assertTrue(itemList.contains(expectedItem), "Purchased item list does not contain the added item.");
		
		Amount expectedPrice = new Amount(itemPrice.add(vatTax).getValue());
		assertTrue(totalPrice.equals(expectedPrice),
				"Incorrect total price " + totalPrice.getValue() + ", expected " + expectedPrice.getValue());
		
		Amount expectedVat = new Amount(vatTax.getValue());
		assertTrue(totalVat.equals(expectedVat),
				"Incorrect total vat " + totalVat.getValue() + ", expected " + expectedVat.getValue());

	}
	
	@Test 
	void testAddMultipleIdenticalItemsToSale() {
		Amount itemPrice = new Amount(5);
		Amount itemVatRate = stdVatRate;
		Amount vatTax = itemPrice.multiply(itemVatRate);

		ItemDescription newItem = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(123));

		sale.addItemToSale(newItem, 1);
		sale.addItemToSale(newItem, 1);
		int purchasedQuantity = 2;

		PriceInformation priceInfo = sale.getPriceInformation();
		Amount totalPrice = priceInfo.getTotalPrice();
		Amount totalVat = priceInfo.getTotalVat();
		
		Receipt saleInfo = sale.processSale(new Amount(100), new Amount(100));
		List<PurchasedItemInformation> itemList = saleInfo.getListOfSoldItems();
		
		PurchasedItemInformation expectedItem = new Item(newItem, purchasedQuantity).getItemInformation();
		assertTrue(itemList.contains(expectedItem), "Purchased item list does not contain the added item.");
		
		Amount expectedPrice = new Amount(itemPrice.add(vatTax).getValue()).multiply(2);
		assertTrue(totalPrice.equals(expectedPrice),
				"Incorrect total price " + totalPrice.getValue() + ", expected " + expectedPrice.getValue());
		
		Amount expectedVat = new Amount(vatTax.getValue()).multiply(2);
		assertTrue(totalVat.equals(expectedVat),
				"Incorrect total vat " + totalVat.getValue() + ", expected " + expectedVat.getValue());
	}
	
	@Test 
	void testAddMultipleDifferentItemsToSale() {
		Amount applePrice = new Amount(5);
		Amount coffeePrice = new Amount(42);
		Amount appleVatRate = stdVatRate;
		Amount coffeeVatRate = stdVatRate;
		Amount appleVatTax = applePrice.multiply(appleVatRate);
		Amount coffeeVatTax = coffeePrice.multiply(coffeeVatRate);

		ItemDescription itemApple = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(123));
		ItemDescription itemCoffee = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(666));

		sale.addItemToSale(itemApple, 1);
		sale.addItemToSale(itemCoffee, 1);

		PriceInformation priceInfo = sale.getPriceInformation();
		Amount totalPrice = priceInfo.getTotalPrice();
		Amount totalVat = priceInfo.getTotalVat();
		
		Receipt saleInfo = sale.processSale(new Amount(100), new Amount(100));
		List<PurchasedItemInformation> itemList = saleInfo.getListOfSoldItems();
		

		PurchasedItemInformation expectedItemApple = new Item(itemApple, 1).getItemInformation();
		assertTrue(itemList.contains(expectedItemApple), "Purchased item list does not contain the added item.");
		
		PurchasedItemInformation expectedItemOrange = new Item(itemCoffee, 1).getItemInformation();
		assertTrue(itemList.contains(expectedItemOrange), "Purchased item list does not contain the added item.");

		Amount expectedPrice = new Amount(((applePrice.add(appleVatTax)).add(coffeePrice.add(coffeeVatTax))).getValue());
		assertTrue(totalPrice.equals(expectedPrice),
				"Incorrect total price " + totalPrice.getValue() + ", expected " + expectedPrice.getValue());
		
		Amount expectedVat = new Amount(appleVatTax.add(coffeeVatTax).getValue());
		assertTrue(totalVat.equals(expectedVat),
				"Incorrect total vat " + totalVat.getValue() + ", expected " + expectedVat.getValue());
	}
	
	@Test
	void testAddMultipleItemsOneOperation() {
		Amount itemPrice = new Amount(5);
		Amount itemVatRate = stdVatRate;
		Amount vatTax = itemPrice.multiply(itemVatRate);

		ItemDescription newItem = creator.getInventorySystem().retrieveItemDescription(new IdentificationNumber(123));

		int purchasedQuantity = 4;
		sale.addItemToSale(newItem, purchasedQuantity);

		PriceInformation priceInfo = sale.getPriceInformation();
		Amount totalPrice = priceInfo.getTotalPrice();
		Amount totalVat = priceInfo.getTotalVat();
		
		Receipt saleInfo = sale.processSale(new Amount(100), new Amount(100));
		List<PurchasedItemInformation> itemList = saleInfo.getListOfSoldItems();
		
		PurchasedItemInformation expectedItem = new Item(newItem, purchasedQuantity).getItemInformation();
		assertTrue(itemList.contains(expectedItem), "Purchased item list does not contain the added item.");
		
		Amount expectedPrice = new Amount(itemPrice.add(vatTax).getValue()).multiply(purchasedQuantity);
		assertTrue(totalPrice.equals(expectedPrice),
				"Incorrect total price " + totalPrice.getValue() + ", expected " + expectedPrice.getValue());
		
		Amount expectedVat = new Amount(vatTax.getValue()).multiply(purchasedQuantity);
		assertTrue(totalVat.equals(expectedVat),
				"Incorrect total vat " + totalVat.getValue() + ", expected " + expectedVat.getValue());
	}

}
