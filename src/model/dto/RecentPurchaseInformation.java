package model.dto;

/**
 * A data container with information about the most recently purchased item
 * and running total.
 */
public final class RecentPurchaseInformation {
	private final PurchasedItemInformation recentlyPurchasedItem;
	private final PriceInformation runningTotal;
	
	/**
	 * Constructs a new container with information about the most recently
	 * purchased item and running total. 
	 * @param recentlyPurchasedItem The most recently purchased item in the sale.
	 * @param runningTotal Up-to-date information about the price of the sale.
	 */
	public RecentPurchaseInformation(PurchasedItemInformation recentlyPurchasedItem, PriceInformation runningTotal) {
		this.recentlyPurchasedItem = recentlyPurchasedItem;
		this.runningTotal = runningTotal;
	}
	
	/**
	 * Returns a data container with information about the most
	 * recently processed item. 
	 * @return An {@link PurchasedItemInformation} object.
	 */
	public PurchasedItemInformation getLatestItemInformation() {
		return recentlyPurchasedItem;
	}
	
	/**
	 * Returns the current price information of this sale. 
	 * @return An {@link PriceInformation} object.
	 */
	public PriceInformation getRunningTotal() {
		return runningTotal;
	}
	
	/**
	 * Gives a string representation containing information about the most recently
	 * purchased item and the running total.
	 */
	public String toString() {
		return "["+ recentlyPurchasedItem.toString() + "]" + "\n" + "Total: " + runningTotal.getTotalPrice() + "\n";
		
	}
}
