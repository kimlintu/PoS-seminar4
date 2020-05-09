package model.util;

/**
 * Represents an identification number.
 */
public class IdentificationNumber {
	private long number;
	
	/**
	 * Constructs an id and initializes its value to the 
	 * provided <code>number</code>.
	 * @param number The value that this <code>IdentificationNumber</code> will have.
	 */
    public IdentificationNumber(long number) {
    	this.number = number;
    }
    
    /**
     * Constructs a <code>IdentificationNumber</code> that represents
     * the same value as the passed object.
     * @param id The <code>IdentificationNumber</code> that should be
     * copied.
     */
    public IdentificationNumber(IdentificationNumber id) {
    	this.number = id.getID();
    }
    
    /**
     * Returns the number that the <code>IdentificationNumber</code> is representing.
     * @return The number as a <code>long</code> data type.
     */
    public long getID() {
    	return number;
    }
    
    /**
     * Compares the numbers of two ids. 
     * 
     * @param id One of the <code>IdentificationNumber</code> to compare.
     * @return Returns <code>true</code> if the numbers that the <code>IdentificationNumber</code> 
     * objects are representing are equal, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object anObject) {
    	if(anObject instanceof IdentificationNumber) {
    		IdentificationNumber otherID = (IdentificationNumber) anObject;
    		return (this.number == otherID.getID());
    	}
    	
    	return false;
    }
    
    /**
     * This ID's value as a string.
     */
    public String toString() {
    	return "" + number;
    }
    
}
