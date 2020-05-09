package integration.dbhandler;

import model.util.IdentificationNumber;

/**
 * Exception that is thrown when an invalid item ID was used to retrieve an item
 * description from the external inventory system.
 */
public class InvalidItemIDException extends Exception {
	private IdentificationNumber invalidID;

	/**
	 * Constructs a new instance of this exception with a a message that specifies
	 * the invalid item ID.
	 * 
	 * @param invalidID The ID that was used in the search.
	 */
	public InvalidItemIDException(IdentificationNumber invalidID) {
		super("User-entered item ID \'" + invalidID + "\' did not correspond to any item in inventory.");

		this.invalidID = invalidID;
	}

	/**
	 * @return the ID that was used in the search.
	 */
	public IdentificationNumber getInvalidID() {
		return invalidID;
	}
}
