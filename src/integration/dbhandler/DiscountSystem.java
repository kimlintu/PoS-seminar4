package integration.dbhandler;

import java.util.Hashtable;
import java.util.List;

import integration.dbhandler.discount.Discount;
import integration.dbhandler.discount.PriceDiscount;
import integration.dbhandler.discount.QuantityDiscount;
import model.dto.PurchasedItemInformation;
import model.util.Amount;
import model.util.IdentificationNumber;

/**
 * External system that stores the available discounts. This implementation only
 * has discounts for specific items.
 */
public class DiscountSystem {
	private Hashtable<IdentificationNumber, Discount> itemDiscounts;

	private static final DiscountSystem DISCOUNT_SYSTEM = new DiscountSystem();

	/**
	 * Constructor. Creates a new table with items and their corresponding
	 * discounts.
	 */
	private DiscountSystem() {
		itemDiscounts = new Hashtable<>();

		itemDiscounts.put(new IdentificationNumber(666), new QuantityDiscount((short) 3, new Amount(0.2)));
		itemDiscounts.put(new IdentificationNumber(876), new PriceDiscount(new Amount(0.4)));
	}

	/**
	 * Returns a table of items with their corresponding discounts. The included
	 * items will only be items that is contained in the provided item list.
	 * 
	 * @param itemList A list of items that was purchased in the sale.
	 * @return a table of items and their available discount.
	 */
	public Hashtable<IdentificationNumber, Discount> getDiscounts(List<PurchasedItemInformation> itemList) {
		Hashtable<IdentificationNumber, Discount> availableDiscounts = new Hashtable<>();

		for (PurchasedItemInformation item : itemList) {
			IdentificationNumber id = item.getItemDescription().getID();
			if (itemDiscounts.containsKey(id)) {
				Discount discount = itemDiscounts.get(id);

				if (!(discount instanceof QuantityDiscount
						&& item.getQuantity() < ((QuantityDiscount) discount).getLimit())) {
					if (discount instanceof QuantityDiscount) {
						int quantity = item.getQuantity();
						((QuantityDiscount) discount).setQuantity(quantity);
					}
					availableDiscounts.put(id, discount);
				}
			}
		}

		return availableDiscounts;
	}

	/**
	 * @return the instance of this class as a singleton.
	 */
	public static DiscountSystem getSystem() {
		return DISCOUNT_SYSTEM;
	}
}
