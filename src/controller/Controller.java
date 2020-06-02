package controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import integration.cashregister.CashRegister;
import integration.dbhandler.AccountingSystem;
import integration.dbhandler.DiscountSystem;
import integration.dbhandler.InvalidItemIDException;
import integration.dbhandler.InventoryException;
import integration.dbhandler.InventorySystem;
import integration.dbhandler.SaleLog;
import integration.dbhandler.SystemCreator;
import integration.dbhandler.data.ItemDescription;
import integration.dbhandler.discount.Discount;
import integration.printer.Printer;
import model.dto.PriceInformation;
import model.dto.PurchasedItemInformation;
import model.dto.Receipt;
import model.dto.RecentPurchaseInformation;
import model.observer.CurrentSaleObserver;
import model.pos.Sale;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * Handles all the system operations in the program.
 */
public class Controller {
	private InventorySystem inventorySystem;
	private AccountingSystem accountingSystem;
	private DiscountSystem discountSystem;
	private SaleLog saleLog;

	private Printer printer;
	private CashRegister cashRegister;

	private Sale currentSale;

	private List<CurrentSaleObserver> saleObservers;

	/**
	 * Creates a new instance and initializes references to the external systems
	 * {@link InventorySystem}, {@link DiscountSystem}, {@link AccountingSystem},
	 * {@link SaleLog}, {@link Printer} and {@link CashRegister}.
	 * 
	 * @param creator A {@link SystemCreator} object that has references to all the
	 *                external systems.
	 */
	public Controller(SystemCreator creator) {
		inventorySystem = creator.getInventorySystem();
		accountingSystem = creator.getAccountingSystem();
		discountSystem = creator.getDiscountSystem();
		saleLog = creator.getSaleLog();

		printer = new Printer();
		cashRegister = new CashRegister();

		saleObservers = new ArrayList<>();
	}

	/**
	 * Starts a new sale and initializes the <code>Sale</code> object in this
	 * controller with that new sale. Also adds this controllers
	 * {@link CurrentSaleObserver}s to the sale object.
	 */
	public void startSale() {
		currentSale = new Sale();
		currentSale.addSaleObservers(saleObservers);
	}

	/**
	 * Signals the program that all items have been scanned.
	 * 
	 * @return Returns information about the total price, including total VAT tax.
	 */
	public PriceInformation endSale() {
		return currentSale.getPriceInformation();
	}
	
	/**
	 * Applies discounts that are available for specific items.
	 * @return the discounted price information.
	 */
	public PriceInformation applyDiscounts() {
		Hashtable<IdentificationNumber, Discount> itemDiscounts = discountSystem.getDiscounts(currentSale.getImmutableItemList());
		currentSale.applyItemDiscounts(itemDiscounts);
		
		return currentSale.getPriceInformation();
	}

	/**
	 * Processes the item by either adding the item to the sale, or updating its
	 * quantity if an identical item has already been processed.
	 * 
	 * @param itemID   The unique id for the item that should be processed.
	 * @param quantity Amount of items being processed.
	 * @throws InvalidItemIDException   If the specified item ID did not correspond
	 *                                  to any item in the inventory system.
	 * @throws OperationFailedException If the item description could not be
	 *                                  retrieved from the external inventory
	 *                                  system.
	 * @return A {@link RecentPurchaseInformation} object containing information
	 *         about the most recently purchased item and the running total.
	 */
	public RecentPurchaseInformation processItem(IdentificationNumber itemID, int quantity)
			throws InvalidItemIDException, OperationFailedException {
		ItemDescription itemDescription;

		try {
			itemDescription = inventorySystem.retrieveItemDescription(itemID);

			PurchasedItemInformation itemInfo = currentSale.addItemToSale(itemDescription, quantity);
			RecentPurchaseInformation recentPurchase = new RecentPurchaseInformation(itemInfo,
					currentSale.getPriceInformation());
			return recentPurchase;
		} catch (InventoryException e) {
			throw new OperationFailedException("Could not retrieve item description.", e);
		}
	}

	/**
	 * Processes the sale by updating the balance in the register, creating a
	 * receipt and sending the sale information to the relevant external systems.
	 * Finally, it prints the receipt and returns the amount of change to give to
	 * the customer.
	 * 
	 * @throws OperationFailedException If the item description could not be
	 *                                  retrieved from the external inventory
	 *                                  system.
	 * @param amountPaid The amount paid by the customer.
	 * @return The amount of change to be received by the customer.
	 */
	public Amount processSale(Amount amountPaid) {
		Amount totalPrice = currentSale.getPriceInformation().getTotalPrice();
		Amount amountOfChange = amountPaid.subtract(totalPrice);

		updateBalanceInCashRegister(totalPrice);

		Receipt receipt = currentSale.processSale(amountPaid, amountOfChange);

		try {
			inventorySystem.updateQuantityOfItems(receipt);
			accountingSystem.updateAccounting(receipt);
			saleLog.logSale(receipt);
		} catch (InventoryException e) {
			throw new OperationFailedException(e.getMessage(), e);
		}

		printer.printReceipt(receipt);

		return amountOfChange;
	}

	/**
	 * Adds the specified observer to this controller's list of sale observers. The
	 * observer will be notified when a sale has been processed.
	 * 
	 * @param observer The {@link CurrentSaleObserver} that should get notified.
	 */
	public void addSaleObserver(CurrentSaleObserver observer) {
		saleObservers.add(observer);
	}

	private Amount updateBalanceInCashRegister(Amount totalPrice) {
		cashRegister.depositAmountToRegister(totalPrice);

		return cashRegister.getBalance();
	}
}
