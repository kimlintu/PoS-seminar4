package integration.dbhandler.discount;

import model.util.Amount;

public class QuantityDiscount implements Discount {
	private final Amount rate;
	private final short limit;

	public QuantityDiscount(short limit, Amount rate) {
		this.limit = limit;
		this.rate = rate;
	}

	@Override
	public Amount getRate() {
		return rate;
	}

	public String toString() {
		return "Quantity discount! " + rate.multiply(100).toString() + "% off!";
	}

	public int getLimit() {
		return limit;
	}
}
