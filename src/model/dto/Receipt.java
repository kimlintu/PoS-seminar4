package model.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.pos.Item;
import model.util.Amount;

/**
 * An immutable data container with information about the completed sale, store
 * and the time of the sale.
 */
public class Receipt {
	private final List<PurchasedItemInformation> itemList;
	private final PriceInformation priceInfo;
	private final Amount amountPaid;
	private final Amount changeAmount;
	private final Store store;
	private final LocalTime timeOfSale;
	private final LocalDate dateOfSale;

	/**
	 * Constructs an instance of <code>Receipt</code> that contains information
	 * about the sale, payment amount, change amount, time of sale and store
	 * information.
	 * 
	 * @param itemList     A list containing the sold items.
	 * @param priceInfo    Price information from the sale.
	 * @param amountPaid   The amount paid by the customer in the sale.
	 * @param changeAmount The amount of change the customer should receive after
	 *                     payment.
	 */
	public Receipt(List<Item> itemList, PriceInformation priceInfo, Amount amountPaid, Amount changeAmount) {
		this.itemList = new ArrayList<>();
		this.priceInfo = priceInfo;
		this.amountPaid = amountPaid;
		this.changeAmount = changeAmount;
		this.timeOfSale = LocalTime.now();
		this.dateOfSale = LocalDate.now();

		this.store = new Store("Real Store", "Real Street 123");

		createImmutableItemList(itemList);
	}

	/**
	 * Returns a list that contains immutable data objects of the sold items.
	 * 
	 * @return An array list.
	 */
	public List<PurchasedItemInformation> getListOfSoldItems() {
		return itemList;
	}

	/**
	 * Returns the price information of the sale.
	 * 
	 * @return A {@link PriceInformation} object.
	 */
	public PriceInformation getPriceInfo() {
		return priceInfo;
	}

	/**
	 * Returns the amount paid by the customer.
	 * 
	 * @return The amount paid as an <code>Amount</code>.
	 */
	public Amount getAmountPaid() {
		return amountPaid;
	}

	/**
	 * Returns the amount of change that is owed to the customer.
	 * 
	 * @return The amount of change as an <code>Amount</code>.
	 */
	public Amount getAmountOfChange() {
		return changeAmount;
	}

	/**
	 * Returns the time that the sale was started.
	 * 
	 * @return Time of the sale as a <code>LocalTime</code> object.
	 */
	public LocalTime getTimeOfSale() {
		return timeOfSale;
	}

	/**
	 * Returns an instance of a {@link Store} object that contains information about
	 * the store's address and name.
	 * 
	 * @return A <code>Store</code> object.
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * Creates a list with immutable item objects from the items that has been sold.
	 * 
	 * @param itemList The list of items that was sold.
	 * @return a new list containing immutable data about each sold item
	 */
	private void createImmutableItemList(List<Item> itemList) {
		for (Item i : itemList) {
			this.itemList.add(i.getItemInformation());
		}
	}

	/**
	 * The string representation for the receipt.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("-----------------Receipt-----------------\n");
		sb.append(String.format("%s %35s\n\n", timeOfSale.withNano(0).withSecond(0), dateOfSale));
		sb.append(String.format("%-15s %-15s %s", "name", "qty*price", "total") + "\n\n");
		for (PurchasedItemInformation productInfo : itemList) {
			sb.append(productInfo.toString() + "\n");
		}
		sb.append("\n-----------------------------------------\n");
		sb.append(String.format("%s\n", priceInfo.toString()));
		sb.append("-----------------------------------------\n");
		sb.append("Amount paid: " + amountPaid + "\n");
		sb.append("Change received: " + changeAmount + "\n");
		sb.append(String.format("\n%30s\n\n", "Thank you, come again!"));
		sb.append(String.format("%27s \n%30s\n", "Store: " + store.getName(), "Address: " + store.getAddress()));
		return sb.toString();
	}
}
