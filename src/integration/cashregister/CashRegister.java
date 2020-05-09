package integration.cashregister;

import java.util.Random;

import model.util.Amount;

/**
 * This class represents the cash register system.
 *
 */
public class CashRegister {
	private Random random = new Random();
	private Amount balance = new Amount(random.nextDouble() * 500 + 500);
	
	/**
	 * Adds the specified amount to the cash registers balance.
	 * @param amountToAdd The amount to add to the register balance.
	 * @return The current amount stored in the register after the deposit.
	 */
	public Amount depositAmountToRegister(Amount amountToAdd) {
		balance = balance.add(amountToAdd);
		
		return balance;
	}
	
	/**
	 * Withdraws the specified amount from the cash registers balance.
	 * @param amountToSubtract The amount to withdraw from the register.
	 * @return The current amount left in the register after the withdrawal.
	 */
	public Amount withdrawAmountFromRegister(Amount amountToSubtract) {
		balance = balance.subtract(amountToSubtract);
		
		return balance;
	}
	
	/**
	 * Returns the current balance in the register.
	 * @return The balance as an <code>Amount</code>.
	 */
	public Amount getBalance() {
		return balance;
	}
}
