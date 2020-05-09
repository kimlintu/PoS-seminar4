package integration.dbhandler;

/**
 * An exception that gets thrown if an error was encountered when connecting to
 * the external inventory system.
 */
public class InventoryDBException extends RuntimeException {
	
	/**
	 * Constructs a new instance that contains the specified message.
	 * @param msg   String explaining this exception.
	 */
	public InventoryDBException(String msg) {
		super(msg);
	}
}
