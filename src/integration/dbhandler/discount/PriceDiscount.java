package integration.dbhandler.discount;

import model.util.Amount;

public class PriceDiscount implements Discount{
	private final Amount rate;
	
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
