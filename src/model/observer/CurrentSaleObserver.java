package model.observer;

import model.util.Amount;

/**
 * An observer for the sale. Receives notifications when the payment 
 * for the sale has been registered.
 */
public interface CurrentSaleObserver {
	
	/**
	 * Gets called when a new payment gets registered.
	 * @param amountPaid The amount paid in the sale.
	 */
	public void newPayment(Amount amountPaid);
}
