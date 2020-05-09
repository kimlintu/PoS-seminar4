package integration.dbhandler.data;

/**
 * This class represents a item data object in the "database". It contains the description
 * and the available quantity of the item. Its attributes are mutable so that they could
 * be updated in the future.
 *
 */
public class ItemData {
	private ItemDescription description;
	private int availableQuantity;

	/**
	 * Creates a new item data object that stores the description and stock quantity. 
	 * @param description An {@link ItemDescription} that contains the description of the item.
	 * @param availableQuantity The amount of the item that's in stock.
	 */
	public ItemData(ItemDescription description, int availableQuantity) {
		this.description = description;
		this.availableQuantity = availableQuantity;
	}
	
	/**
	 * Returns the description of the item data object..
	 * @return An {@link ItemDescription} object.
	 */
	public ItemDescription getItemDescription() {
		return description;
	}
	
	/**
	 * Returns the available quantity of the item.
	 * @return The quantity in stock as an <code>int</code>.
	 */
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	
	/**
	 * Increases the stock quantity of the stored item.
	 * @param quantityToAdd How much to increase quantity.
	 */
	public void increaseQuantity(int quantityToAdd) {
		availableQuantity += quantityToAdd;
	}
	
	/**
	 * Decreases the stock quantity of the stored item.
	 * @param quantityToSubtract How much to decrease quantity.
	 */
	public void decreaseQuantity(int quantityToSubtract) {
		availableQuantity -= quantityToSubtract;
	}
	
	/**
	 * Checks if the item has available stock.
	 * @return <code>true</code> if the quantity in stock is greater than 0, otherwise false.
	 */
	public boolean inStock() {
		return availableQuantity > 0;
	}
}
