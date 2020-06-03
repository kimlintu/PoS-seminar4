package integration.dbhandler.discount;

import model.util.Amount;

/**
 * A discount that can be applied to an item.
 */
public interface Discount {
	
	/**
	 * @return the rate for this discount.
	 */
	public Amount getRate();
}
