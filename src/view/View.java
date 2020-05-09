package view;

import java.io.IOException;
import java.util.Random;

import controller.Controller;
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
	
	private ErrorMessageHandler errorMsgHandler;
	private ErrorLogHandler errorLog;

	public View(Controller controller) {
		this.controller = controller;
		
		errorMsgHandler = new ErrorMessageHandler();
		
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
	 */
	public RecentPurchaseInformation enterItemIdentifier(IdentificationNumber itemID, int quantity)
			throws InvalidItemIDException {
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

	public void testRun() {
		Random rand = new Random();
		IdentificationNumber validIDs[] = { new IdentificationNumber(123), new IdentificationNumber(666),
				new IdentificationNumber(492), new IdentificationNumber(876) };

		startSale();

		try {
			for (int i = 0; i < validIDs.length; i++) {
				int quantity = rand.nextInt(9) + 1;

				System.out.println("Purchasing " + quantity + " item(s) with id " + validIDs[i].toString());
				RecentPurchaseInformation recentSaleInformation = enterItemIdentifier(new IdentificationNumber(2), quantity);
				System.out.println(recentSaleInformation);
			}

			PriceInformation totalPriceInfo = endSale();
			System.out.println("[" + totalPriceInfo + "]" + "\n");
			Amount amountPaid = totalPriceInfo.getTotalPrice().add(new Amount(rand.nextDouble() * 200 + 1));
			System.out.println("Customer pays " + amountPaid);

			System.out.println("Processing sale and printing receipt..\n");
			enterAmountPaid(amountPaid);
		} catch (InvalidItemIDException e) {
			errorMsgHandler.showErrorMessage("Item with ID \'" + e.getInvalidID() + "\' is invalid.");
			errorLog.logException(e);
		}
	}
}
