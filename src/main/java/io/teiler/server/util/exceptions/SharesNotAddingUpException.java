package io.teiler.server.util.exceptions;

/**
 * Exception denoting that Share-/Profiteer-shares do not add up to an expected value.
 * 
 * @author pbaechli
 */
public class SharesNotAddingUpException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public SharesNotAddingUpException() {
        super("SHARES_DONT_ADD_UP");
    }
    
}
