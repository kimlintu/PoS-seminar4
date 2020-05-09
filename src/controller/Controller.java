package controller;

import integration.cashregister.CashRegister;
import integration.dbhandler.AccountingSystem;
import integration.dbhandler.InvalidItemIDException;
import integration.dbhandler.InventoryDBException;
import integration.dbhandler.InventorySystem;
import integration.dbhandler.SaleLog;
import integration.dbhandler.SystemCreator;
import integration.dbhandler.data.ItemDescription;
import integration.printer.Printer;
import model.dto.PriceInformation;
import model.dto.PurchasedItemInformation;
import model.dto.Receipt;
import model.dto.RecentPurchaseInformation;
import model.pos.Sale;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * Handles all the system operations in the program.
 */
public class Controller {
	private InventorySystem inventorySystem;
	private AccountingSystem accountingSystem;
	private SaleLog saleLog;

	private Printer printer;
	private CashRegister cashRegister;

	private Sale currentSale;

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
		saleLog = creator.getSaleLog();

		printer = new Printer();
		cashRegister = new CashRegister();
	}

	/**
	 * Starts a new sale and initializes the <code>Sale</code> object in this
	 * controller with that new sale.
	 */
	public void startSale() {
		currentSale = new Sale();
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
	 * Processes the item by either adding the item to the sale, or updating its
	 * quantity if an identical item has already been processed.
	 * 
	 * @param itemID   The unique id for the item that should be processed.
	 * @param quantity Amount of items being processed.
	 * @throws InvalidItemIDException If the specified item ID did not correspond to
	 *                                any item in the inventory system.
	 * @throws OperationFailedException If the operation failed to complete.
	 * @return A {@link RecentPurchaseInformation} object containing information
	 *         about the most recently purchased item and the running total.
	 */
	public RecentPurchaseInformation processItem(IdentificationNumber itemID, int quantity)
			throws InvalidItemIDException {
		ItemDescription itemDescription;
		
		try {
			itemDescription = inventorySystem.retrieveItemDescription(itemID);
		} catch (InventoryDBException e) {
			throw new OperationFailedException(e.getMessage(), e);
		}

		PurchasedItemInformation itemInfo = currentSale.addItemToSale(itemDescription, quantity);
		RecentPurchaseInformation recentPurchase = new RecentPurchaseInformation(itemInfo,
				currentSale.getPriceInformation());

		return recentPurchase;
	}

	/**
	 * Processes the sale by updating the balance in the register, creating a
	 * receipt and sending the sale information to the relevant external systems.
	 * Finally, it prints the receipt and returns the amount of change to give to
	 * the customer.
	 * 
	 * @param amountPaid The amount paid by the customer.
	 * @return The amount of change to be received by the customer.
	 */
	public Amount processSale(Amount amountPaid) {
		Amount totalPrice = currentSale.getPriceInformation().getTotalPrice();
		Amount amountOfChange = amountPaid.subtract(totalPrice);

		updateBalanceInCashRegister(totalPrice);

		Receipt receipt = currentSale.processSale(amountPaid, amountOfChange);

		accountingSystem.updateAccounting(receipt);
		saleLog.logSale(receipt);
		inventorySystem.updateQuantityOfItems(receipt);

		printer.printReceipt(receipt);

		return amountOfChange;
	}

	private Amount updateBalanceInCashRegister(Amount totalPrice) {
		cashRegister.depositAmountToRegister(totalPrice);

		return cashRegister.getBalance();
	}
}
