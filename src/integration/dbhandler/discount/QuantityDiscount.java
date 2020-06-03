package integration.dbhandler.discount;

import model.util.Amount;

/**
 * A item discount that depends on the amount of items bought.
 */
public class QuantityDiscount implements Discount {
	private Amount rate;
	private final Amount maxRate;

	private final short limit;
	private int quantity;

	/**
	 * Constructor.
	 * 
	 * @param limit   The minimum quantity of the bought item for this discount to
	 *                be applied.
	 * @param maxRate The maximum rate this discount can have.
	 */
	public QuantityDiscount(short limit, Amount maxRate) {
		this.limit = limit;
		this.maxRate = maxRate;
	}

	@Override
	public Amount getRate() {
		rate = calculateRate();
		return rate;
	}

	private Amount calculateRate() {
		double cRate = new Amount(((double) (2.0 * quantity)) / 100).getValue().doubleValue();
		double mRate = maxRate.getValue().doubleValue();
		double finalRate = Math.min(mRate, cRate);

		return new Amount(finalRate);
	}

	public String toString() {
		return "Quantity discount! " + rate.multiply(100).toString() + "% off!";
	}

	/**
	 * @param quantity The quantity of the item that this discount will be applied
	 *                 to.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the minimum quantity that is needed for this discount to be applied.
	 */
	public short getLimit() {
		return limit;
	}
}
