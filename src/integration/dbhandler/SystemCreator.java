package integration.dbhandler;

/**
 * Constructs objects for all the external system handlers.
 */
public class SystemCreator {
	private InventorySystem inventorySystem;
	private AccountingSystem accountingSystem;
	private DiscountSystem discountSystem;
	private SaleLog saleLog;
	
	private static final SystemCreator SYSTEM_CREATOR = new SystemCreator();
	
	/**
	 * Constructs a system creator and creates new objects
	 * for the external systems.
	 */
	private SystemCreator() {
		inventorySystem = InventorySystem.getSystem();
		accountingSystem = AccountingSystem.getSystem();
		discountSystem = DiscountSystem.getSystem();
		saleLog = SaleLog.getSystem();
	}
	
	/**
	 * Returns a reference to the {@link InventorySystem} object
	 * created by this creator.
	 * @return A reference to an {@link InventorySystem} object.
	 */
	public InventorySystem getInventorySystem() {
		return inventorySystem;
	}
	
	/**
	 * Returns a reference to the {@link AccountingSystem} object
	 * created by this creator.
	 * @return A reference to an {@link AccountingSystem} object.
	 */
	public AccountingSystem getAccountingSystem() {
		return accountingSystem;
	}
	
	public DiscountSystem getDiscountSystem() {
		return discountSystem;
	}
	
	/**
	 * Returns a reference to the {@link SaleLog} object
	 * created by this creator.
	 * @return A reference to an {@link SaleLog} object.
	 */
	public SaleLog getSaleLog() {
		return saleLog;
	}
	
	/**
	 * @return the instance of this class as a singleton.
	 */
	public static SystemCreator getCreator() {
		return SYSTEM_CREATOR;
	}
}
