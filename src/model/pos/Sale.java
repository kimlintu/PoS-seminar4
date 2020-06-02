package model.pos;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import integration.dbhandler.InventorySystem;
import integration.dbhandler.data.ItemDescription;
import integration.dbhandler.discount.Discount;
import model.dto.PriceInformation;
import model.dto.PurchasedItemInformation;
import model.dto.Receipt;
import model.observer.CurrentSaleObserver;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * This class represents the ongoing sale. It has an internal array list that
 * stores the sold items.
 */
public class Sale {
	private List<Item> itemList;
	private TotalPrice totalPrice;

	private List<CurrentSaleObserver> saleObservers = new ArrayList<>();

	/**
	 * Creates a new instance. The instance will have a list that contains the items
	 * that's being purchased and the total price of the sale
	 */
	public Sale() {
		itemList = new ArrayList<>();
		totalPrice = new TotalPrice();
	}

	/**
	 * Creates a new {@link Item} object containing the <code>itemDescription</code>
	 * and the <code>quantity</code> and adds it to the internal item list. If an
	 * identical item already exists in the list, the program will instead update
	 * the quantity of that item.
	 * 
	 * @param itemDescription   description of the item that's stored in the
	 *                          {@link InventorySystem}.
	 * @param purchasedQuantity Amount of the corresponding item being purchased.
	 * 
	 * @return Information about the most recently purchased item.
	 */
	public PurchasedItemInformation addItemToSale(ItemDescription itemDescription, int purchasedQuantity) {
		Item purchasedItem = new Item(itemDescription, purchasedQuantity);

		if (itemList.contains(purchasedItem)) {
			Item itemInList = getItemFromList(purchasedItem);

			updateQuantityOfItemInList(itemInList, purchasedQuantity);
		} else {
			addItemToList(purchasedItem);
		}

		return purchasedItem.getItemInformation();
	}

	/**
	 * Updates the total price from the items that have been sold so far.Returns
	 * price information about this sale.
	 * 
	 * @return A {@link PriceInformation} object.
	 */
	public PriceInformation getPriceInformation() {
		updateTotalPrice();
		return totalPrice.getPriceInfo();
	}

	/**
	 * Complete the sale and create a receipt containing the complete sale
	 * information. Also notifies the observers of the provided payment.
	 * 
	 * @param amountPaid     The amount paid by the customer.
	 * @param amountOfChange The amount of change that the customer should receive.
	 * @return
	 */
	public Receipt processSale(Amount amountPaid, Amount amountOfChange) {
		Receipt receipt = new Receipt(getImmutableItemList(), totalPrice.getPriceInfo(), amountPaid, amountOfChange);

		notifySaleObservers(amountPaid.subtract(amountOfChange));
		return receipt;
	}

	/**
	 * Adds the list of the provided {@link CurrentSaleObserver}s to this object.
	 * 
	 * @param saleObservers A list of <code>CurrentSaleObserver</code>s.
	 */
	public void addSaleObservers(List<CurrentSaleObserver> saleObservers) {
		this.saleObservers.addAll(saleObservers);
	}

	/**
	 * Apply the discounts contained in the table to the item with the corresponding
	 * ID numbers.
	 * 
	 * @param itemDiscounts A table that specifies item IDs and their corresponding
	 *                      discount.
	 */
	public void applyItemDiscounts(Hashtable<IdentificationNumber, Discount> itemDiscounts) {
		for (Item item : itemList) {
			IdentificationNumber id = item.getItemDescription().getID();

			if (itemDiscounts.containsKey(id)) {
				item.applyDiscount(itemDiscounts.get(id));
			}
		}
	}

	/**
	 * Creates a list with immutable item objects from the items that has been sold.
	 * 
	 * @param itemList The list of items that was sold.
	 * @return a new list containing immutable data about each sold item
	 */
	public List<PurchasedItemInformation> getImmutableItemList() {
		List<PurchasedItemInformation> newList = new ArrayList<>();
		for (Item i : itemList) {
			newList.add(i.getItemInformation());
		}

		return newList;
	}

	private void notifySaleObservers(Amount amountPaid) {
		for (CurrentSaleObserver saleObs : saleObservers) {
			saleObs.newPayment(amountPaid);
		}
	}

	private void updateTotalPrice() {
		totalPrice = new TotalPrice();

		for (Item item : itemList) {
			totalPrice.addToTotalPrice(item);
		}
	}

	private void updateQuantityOfItemInList(Item item, int quantity) {
		item.addToQuantity(quantity);
	}

	private Item getItemFromList(Item item) {
		return itemList.get(itemList.indexOf(item));
	}

	private void addItemToList(Item item) {
		itemList.add(item);
	}

}
