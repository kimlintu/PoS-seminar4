package integration.dbhandler;

import java.util.ArrayList;
import java.util.List;

import integration.dbhandler.data.ItemData;
import integration.dbhandler.data.ItemDescription;
import model.dto.PurchasedItemInformation;
import model.dto.Receipt;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * A class that handles the external inventory system. The database connected to
 * this is represented by an internal array list.
 *
 */
public class InventorySystem {
	private List<ItemData> itemDB;
	private final Amount stdVatRate = new Amount(0.16);

	private final IdentificationNumber dbError = new IdentificationNumber(987987);

	/**
	 * Constructs a new object and adds some data entries.
	 */
	InventorySystem() {
		itemDB = new ArrayList<>();

		createDatabaseEntry(new ItemData(
				new ItemDescription("apple", new Amount(5), stdVatRate, new IdentificationNumber(123)), 54));
		createDatabaseEntry(new ItemData(
				new ItemDescription("coffee", new Amount(42), stdVatRate, new IdentificationNumber(666)), 12));
		createDatabaseEntry(new ItemData(
				new ItemDescription("orange juice", new Amount(12), stdVatRate, new IdentificationNumber(492)), 5));
		createDatabaseEntry(new ItemData(
				new ItemDescription("chocolate bar", new Amount(10), stdVatRate, new IdentificationNumber(876)), 1));
	}

	/**
	 * Searches the database(internal ArrayList) for an {@link ItemDescription} with
	 * the specified <code>itemID</code>.
	 * 
	 * @param itemID <code>IdentificationNumber</code> that will be used in the
	 *               search.
	 * @throws InvalidItemIDException If the specified item ID did not correspond to
	 *                                any item in the inventory system.
	 * @return The {@link ItemDescription} if its id matches the
	 *         <code>itemID</code>, otherwise <code>null</code>.
	 * @throws InventoryDBException If a connection to the inventory database could
	 *                              not be established.
	 */
	public ItemDescription retrieveItemDescription(IdentificationNumber itemID)
			throws InvalidItemIDException {
		if (itemID.equals(dbError)) {
			throw new InventoryDBException("Could not establish a connection to the database.");
		}

		for (ItemData itemDataObject : itemDB) {
			if (itemDataObject.getItemDescription().getID().equals(itemID))
				return itemDataObject.getItemDescription();
		}

		throw new InvalidItemIDException(itemID);
	}

	/**
	 * Updates the quantity of the items in the database that was processed in the
	 * sale.
	 * 
	 * @param saleInfo The information about the completed sale. Contains the list
	 *                 of sold items.
	 */
	public void updateQuantityOfItems(Receipt saleInfo) {
		List<PurchasedItemInformation> itemList = saleInfo.getListOfSoldItems();

		for (PurchasedItemInformation itemInfo : itemList) {
			for (ItemData itemData : itemDB) {
				IdentificationNumber searchedItemID = itemInfo.getItemDescription().getID();

				if (matches(searchedItemID, itemData)) {
					itemData.decreaseQuantity(itemInfo.getQuantity());
				}
			}
		}
	}

	/**
	 * Returns the available quantity of an item stored in the database.
	 * 
	 * @param itemID The unique ID of the corresponding item.
	 * @return The available quantity as an <code>int</code>.
	 */
	public int getAvailableQuantityOfItem(IdentificationNumber itemID) {
		for (ItemData itemData : itemDB) {
			if (matches(itemID, itemData)) {
				return itemData.getAvailableQuantity();
			}
		}

		return 0;
	}

	private boolean matches(IdentificationNumber searchedItemID, ItemData itemData) {
		IdentificationNumber storedItemID = itemData.getItemDescription().getID();

		return searchedItemID.equals(storedItemID);
	}

	private void createDatabaseEntry(ItemData itemDataObject) {
		itemDB.add(itemDataObject);
	}
}
