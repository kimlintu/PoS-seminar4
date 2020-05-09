package model.dto;

/**
 * The store that the POS is located in.
 */
public class Store {
	private final String name;
	private final String address;
	
	/**
	 * Constructor.
	 * @param name Name of the store.
	 * @param address Address that the store is located at.
	 */
	public Store(String name, String address) {
		this.name = name;
		this.address = address;
	}

	/**
	 * Returns the name of the store.
	 * @return the name as a <code>String</code>.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the address of the store.
	 * @return the address as a <code>String</code>.
	 */
	public String getAddress() {
		return address;
	}
}
