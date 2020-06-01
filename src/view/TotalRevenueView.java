package view;

import model.observer.CurrentSaleObserver;
import model.util.Amount;

/**
 * Represents a display showing the accumulated revenue from sales
 * since the program was started.
 */
public class TotalRevenueView implements CurrentSaleObserver{
	private Amount totalRevenue;
	
	TotalRevenueView() {
		totalRevenue = new Amount(0);
	}
	
	@Override
	public void newPayment(Amount amountPaid) {
		totalRevenue = totalRevenue.add(amountPaid);
		printState();
	}
	
	private void printState() {
		System.out.println(" -------------------------");
		System.out.println("| [Total revenue: " + totalRevenue + "] |");
		System.out.println(" -------------------------\n");
	}
}
