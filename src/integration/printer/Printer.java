package integration.printer;

import model.dto.Receipt;

/**
 * Represents the printer system.
 */
public class Printer {
	
	/**
	 * Prints the receipt to system output.
	 * @param receipt The receipt to print.
	 */
	public void printReceipt(Receipt receipt) {
		System.out.println(receipt.toString());
	}
}
