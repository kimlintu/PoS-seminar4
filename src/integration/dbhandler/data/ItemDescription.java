package integration.dbhandler.data;

import integration.dbhandler.InventorySystem;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * This class is a data container that contains information pertaining to an
 * item, including its name, ID and its price information.
 */
public class ItemDescription {
	private final String name;
	private final Amount price;
	private final Amount vatRate;
	private final IdentificationNumber id;

	/**
	 * Constructs an item description that is used to describe an unique item that
	 * exists in the {@link InventorySystem}.
	 * 
	 * @param name    Name of the item.
	 * @param price   The price of the item.
	 * @param vatRate The VAT rate of the item.
	 * @param id      An unique id that identifies the item.
	 */
	public ItemDescription(String name, Amount price, Amount vatRate, IdentificationNumber id) {
		this.name = name;
		this.price = price;
		this.vatRate = vatRate;
		this.id = id;
	}

	/**
	 * Returns the item id that's being stored in the description.
	 * 
	 * @return The <code>IdentificationNumber</code> representing the item id.
	 */
	public IdentificationNumber getID() {
		return id;
	}

	/**
	 * Returns the name of the item that this <code>ItemDescription</code> is
	 * describing.
	 * 
	 * @return The item <code>name</code> as a <code>String</code>.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the price of this item, excluding VAT tax.
	 * 
	 * @return the original price as an <code>Amount</code>.
	 */
	public Amount getPrice() {
		return price;
	}

	/**
	 * Returns the VAT rate of this item.
	 * 
	 * @return the VAT rate as an <code>Amount</code>.
	 */
	public Amount getVatRate() {
		return vatRate;
	}

	/**
	 * Compares the item description to an object.
	 * 
	 * @param anObject The object to compare the <code>ItemDescription</code>
	 *                 against.
	 * @return <code>true</code> if the object represents an
	 *         <code>ItemDescription</code> and has an {@link IdentificationNumber}
	 *         equivalent to the other <code>ItemDescription</code>,
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof ItemDescription) {
			ItemDescription i = (ItemDescription) anObject;
			return this.id.equals(i.getID());
		}

		return false;
	}
}
