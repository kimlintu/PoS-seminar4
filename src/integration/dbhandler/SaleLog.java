package integration.dbhandler;

import model.dto.Receipt;

/**
 * This class handles the external sale log.
 *
 */
public class SaleLog {
	private static final SaleLog SALE_LOG = new SaleLog();
	
	/**
	 * Constructor.
	 */
	private SaleLog() {
	}

	/**
	 * Logs the relevant information in the sale log.
	 * 
	 * @param completedSaleInformation Information about the completed sale.
	 */
	public void logSale(Receipt completedSaleInformation) {

	}
	
	/**
	 * @return the instance of this class as a singleton.
	 */
	public static SaleLog getSystem() {
		return SALE_LOG;
	}
}
