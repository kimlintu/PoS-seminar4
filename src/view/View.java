package view;

import java.io.IOException;
import java.util.Random;

import controller.Controller;
import controller.OperationFailedException;
import integration.dbhandler.InvalidItemIDException;
import log.ErrorLogHandler;
import model.dto.PriceInformation;
import model.dto.RecentPurchaseInformation;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * This class replaces the whole view for the program.
 */
public class View {
	private Controller controller;

	private DisplayErrorMessageHandler errorMsgHandler;
	private ErrorLogHandler errorLog;

	/**
	 * Constructor.
	 * 
	 * @param controller The controller for the system.
	 */
	public View(Controller controller) {
		this.controller = controller;
		controller.addSaleObserver(new TotalRevenueView());

		errorMsgHandler = new DisplayErrorMessageHandler();
		try {
			errorLog = new ErrorLogHandler();
		} catch (IOException e) {
			System.out.println("Error when creating the error log handler.");
			e.printStackTrace();
		}
	}

	/**
	 * Starts a new sale.
	 */
	public void startSale() {
		controller.startSale();
	}

	/**
	 * Ends the current ongoing sale.
	 * 
	 * @return a {@link PriceInformation object}, containing information about the
	 *         total price and VAT tax.
	 */
	public PriceInformation endSale() {
		return controller.endSale();
	}

	/**
	 * Processes the item represented by the specified item ID.
	 * 
	 * @param itemID   The ID of the item that should be processed.
	 * @param quantity Quantity of the item being purchased.
	 * @return a {@link RecentPurchaseInformation}
	 * @throws OperationFailedException If this operation failed to complete.
	 */
	public RecentPurchaseInformation enterItemIdentifier(IdentificationNumber itemID, int quantity)
			throws InvalidItemIDException, OperationFailedException {
		return controller.processItem(itemID, quantity);
	}

	/**
	 * Sends the amount paid by the customer.
	 * 
	 * @param amountPaid the amount paid by the customer.
	 */
	public void enterAmountPaid(Amount amountPaid) {
		controller.processSale(amountPaid);
	}

	/**
	 * A test run where no exceptions gets thrown.
	 */
	public void testRun() {
		Random rand = new Random();
		IdentificationNumber itemIDs[] = { new IdentificationNumber(123), new IdentificationNumber(666),
				new IdentificationNumber(492), new IdentificationNumber(876) };

		startSale();

		try {
			for (int itemNr = 0; itemNr < itemIDs.length; itemNr++) {
				int quantity = rand.nextInt(4) + 1;

				System.out.println("*Purchasing " + quantity + " item(s) with id " + itemIDs[itemNr].toString() + "*");
				RecentPurchaseInformation recentSaleInformation = enterItemIdentifier(itemIDs[itemNr], quantity);
				System.out.println(recentSaleInformation);
			}

			PriceInformation totalPriceInfo = endSale();
			System.out.println("[" + totalPriceInfo + "]" + "\n");
			Amount amountPaid = totalPriceInfo.getTotalPrice().add(new Amount(rand.nextDouble() * 200 + 1));
			System.out.println("*Customer pays " + amountPaid + "*");

			System.out.println("*Processing sale and printing receipt..*\n");
			enterAmountPaid(amountPaid);

		} catch (InvalidItemIDException e) {
			errorMsgHandler.showErrorMessage("Item with ID \'" + e.getInvalidID() + "\' is invalid.");
			errorLog.logException(e);

		} catch (OperationFailedException e) {
			errorMsgHandler.showErrorMessage("Sale could not complete, please contact \nthe system administrator.");
			errorLog.logException(e);
		}
	}
	
	public void testRunWithDiscounts() {
		Random rand = new Random();
		IdentificationNumber itemIDs[] = { new IdentificationNumber(123), new IdentificationNumber(666),
				new IdentificationNumber(492), new IdentificationNumber(876) };

		startSale();

		try {
			for (int itemNr = 0; itemNr < itemIDs.length; itemNr++) {
				int quantity = rand.nextInt(10) + 1;

				System.out.println("*Purchasing " + quantity + " item(s) with id " + itemIDs[itemNr].toString() + "*");
				RecentPurchaseInformation recentSaleInformation = enterItemIdentifier(itemIDs[itemNr], quantity);
				System.out.println(recentSaleInformation);
			}

			PriceInformation totalPriceInfo = endSale();
			System.out.println("[" + totalPriceInfo + "]" + "\n");
			
			System.out.println("*Available discounts gets applied to purchased items*");
			PriceInformation discountedPrice = controller.applyDiscounts();
			System.out.println("[" + discountedPrice + "]" + "\n");
			
			Amount amountPaid = totalPriceInfo.getTotalPrice().add(new Amount(rand.nextDouble() * 100 + 1));
			System.out.println("*Customer pays " + amountPaid + "*");

			System.out.println("*Processing sale and printing receipt..*\n");
			enterAmountPaid(amountPaid);

		} catch (InvalidItemIDException e) {
			errorMsgHandler.showErrorMessage("Item with ID \'" + e.getInvalidID() + "\' is invalid.");
			errorLog.logException(e);

		} catch (OperationFailedException e) {
			errorMsgHandler.showErrorMessage("Sale could not complete, please contact \nthe system administrator.");
			errorLog.logException(e);
		}
	}

	/**
	 * A test run where the database encounters an error.
	 */
	public void testRunWithDatabaseError() {
		IdentificationNumber databaseError = new IdentificationNumber(987987);
		startSale();

		try {
			RecentPurchaseInformation recentSaleInformation = enterItemIdentifier(databaseError, 1);
			System.out.println(recentSaleInformation);

			PriceInformation totalPriceInfo = endSale();
			System.out.println("[" + totalPriceInfo + "]" + "\n");
			Amount amountPaid = totalPriceInfo.getTotalPrice().add(new Amount(100));
			System.out.println("Customer pays " + amountPaid);

			System.out.println("Processing sale and printing receipt..\n");
			enterAmountPaid(amountPaid);
		} catch (OperationFailedException e) {
			errorMsgHandler.showErrorMessage("Sale could not complete, please contact \nthe system administrator.");
			errorLog.logException(e);
		} catch (

		InvalidItemIDException e) {
			errorMsgHandler.showErrorMessage("Item with ID \'" + e.getInvalidID() + "\' is invalid.");
			errorLog.logException(e);
		}
	}

	/**
	 * A test run where and invalid ID gets entered and the
	 * {@link InvalidItemIDException} gets thrown.
	 * 
	 */
	public void testRunWithInvalidID(IdentificationNumber invalidID) {
		boolean saleInProgress = true;
		startSale();

		IdentificationNumber itemIDs[] = { new IdentificationNumber(123), new IdentificationNumber(666), invalidID,
				new IdentificationNumber(876) };
		int itemNr = 0;

		while (saleInProgress) {
			try {
				if (itemNr < 4) {
					int quantity = new Random().nextInt(2) + 1;
					
					try {
						System.out.println("*Purchasing " + quantity + " item(s) with id " + itemIDs[itemNr].toString() + "*");
						RecentPurchaseInformation recentPurchase = enterItemIdentifier(itemIDs[itemNr++],
								quantity);
						System.out.println(recentPurchase);
					} catch (InvalidItemIDException e) {
						errorMsgHandler.showErrorMessage("Item with ID \'" + e.getInvalidID() + "\' is invalid. Please try again.");
						errorLog.logException(e);
					}
				} else {
					saleInProgress = false;

					PriceInformation totalPriceInfo = endSale();
					System.out.println("[" + totalPriceInfo + "]" + "\n");
					Amount amountPaid = totalPriceInfo.getTotalPrice()
							.add(new Amount(new Random().nextDouble() * 200 + 1));
					System.out.println("*Customer pays " + amountPaid + "*");

					System.out.println("*Processing sale and printing receipt..*\n");
					enterAmountPaid(amountPaid);
				}
			} catch (OperationFailedException e) {
				errorMsgHandler.showErrorMessage("Sale could not complete, please contact \nthe system administrator.");
				errorLog.logException(e);
			}
		}
	}
}
