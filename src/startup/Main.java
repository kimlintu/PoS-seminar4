package startup;

import controller.Controller;
import integration.dbhandler.SystemCreator;
import view.View;

public class Main {
	public static void main(String[] args) {
		SystemCreator creator = SystemCreator.getCreator();
		
		Controller controller = new Controller(creator);
		View view = new View(controller);

//		view.testRunWithInvalidID(new IdentificationNumber(98734342));
//		view.testRunWithDatabaseError();
//		
//		view.testRun();
//		view.testRun();
//		view.testRun();
		
		view.testRunWithDiscounts();
	}
}
