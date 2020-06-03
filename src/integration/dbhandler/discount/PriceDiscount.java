package integration.dbhandler.discount;

import model.util.Amount;

/**
 * A discount that gets directly applied to an item.
 */
public class PriceDiscount implements Discount{
	private final Amount rate;
	
	/**
	 * Constructor.
	 * @param rate The rate that this discount will have.
	 */
	public PriceDiscount(Amount rate) {
		this.rate = rate;
	}
	
	@Override
	public Amount getRate() {
		return rate;
	}
	
	public String toString() {
		return "Price discount! " + rate.multiply(100).toString() + "% off!";
	}
}
