package model.pos;

import model.dto.PriceInformation;
import model.util.Amount;

/**
 * Represents the total price of the ongoing sale. Includes the total price including
 * VAT tax but also the VAT tax as a separate amount.
 */
public class TotalPrice {
	private Amount totalPriceAmount;
	private Amount totalVatAmount;
	
	/**
	 * Creates a new instance with total price and total VAT tax
	 * at an initial amount of 0.
	 */
	TotalPrice() {
		totalPriceAmount = new Amount(0);
		totalVatAmount = new Amount(0);
	}
	
	/**
	 * Retrieves a new {@link PriceInformation} object containing
	 * the total price and total vat tax.
	 * @return the price information.
	 */
	PriceInformation getPriceInfo() {
		return new PriceInformation(totalPriceAmount, totalVatAmount);
	}
	
	/**
	 * Increments the total price of the sale by the price of an item
	 * times the quantity of the item. Also adds the items VAT tax to
	 * the total.
	 * @param item Item whose price is getting added to the total. 
	 */
	void addToTotalPrice(Item item) {
		Amount itemUnitPrice = item.getUnitPrice();
		Amount itemUnitVat = item.getUnitVatTax();
		
		totalPriceAmount = totalPriceAmount.add(itemUnitPrice.multiply(item.getQuantity()));
		
		addToTotalVat(itemUnitVat.multiply(item.getQuantity()));
	}
	
	private void addToTotalVat(Amount itemVatAmount) {
		totalVatAmount = totalVatAmount.add(itemVatAmount);
	}
}
