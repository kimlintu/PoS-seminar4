package integration.dbhandler;

import model.dto.Receipt;

/**
 * The class handles the external accounting system.
 *
 */
public class AccountingSystem {
	private static final AccountingSystem ACCOUNTING_SYSTEM = new AccountingSystem();
	/**
	 * Constructs a new object.
	 */
	private AccountingSystem() {
	}

	/**
	 * Updates relevant information stored in this system pertaining to the
	 * completed sale.
	 * 
	 * @param completedSaleInformation A DTO with all available information about a
	 *                                 completed sale.
	 */
	public void updateAccounting(Receipt completedSaleInformation) {
	}
	
	/**
	 * @return the instance of this class as a singleton.
	 */
	public static AccountingSystem getSystem() {
		return ACCOUNTING_SYSTEM;
	}
}
