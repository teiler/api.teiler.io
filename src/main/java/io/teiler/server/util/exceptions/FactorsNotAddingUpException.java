package io.teiler.server.util.exceptions;

/**
 * Exception denoting that Share-/Profiteer-factors do not add up to an expected value.
 * 
 * @author pbaechli
 */
public class FactorsNotAddingUpException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public FactorsNotAddingUpException() {
        super("FACTORS_DONT_ADD_UP");
    }
    
}
