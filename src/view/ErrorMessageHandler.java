package view;
/**
 * Handles the error messages displayed to the user of the system. 
 */
public class ErrorMessageHandler {
	
	/**
	 * Displays the specified message to system output.
	 * @param msg The message to display.
	 */
	void showErrorMessage(String msg) {
		System.out.println("ERROR: " + msg);
	}

}
