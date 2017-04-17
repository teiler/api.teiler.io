package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a Profiteer has not been found (and presumably doesn't exist).
 * 
 * @author pbaechli
 */
public class ProfiteerNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ProfiteerNotFoundException() {
        super("PROFITEER_NOT_FOUND");
    }
    
}
