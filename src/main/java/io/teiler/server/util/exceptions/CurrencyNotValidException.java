package io.teiler.server.util.exceptions;

/**
 * Exception denoting that access to some resource(s) has not been authorised.
 *
 * @author lroellin
 */
public class CurrencyNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CurrencyNotValidException() {
        super("CURRENCY_NOT_VALID");
    }

}
