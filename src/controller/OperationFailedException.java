package controller;

/**
 * Signals that a system operation in the controller has failed.
 */
public class OperationFailedException extends RuntimeException {

	/**
	 * Constructs a new instance that contains the specified message.
	 * @param msg   String explaining this exception.
	 */
	public OperationFailedException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new instance that contains the specified message and another
	 * exception, preferably the exception that caused this one to be thrown.
	 * 
	 * @param msg   String explaining this exception.
	 * @param cause Another exception, see above.
	 */
	public OperationFailedException(String msg, Exception cause) {
		super(msg, cause);
	}
}
