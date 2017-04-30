package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a person has not been found (and presumably doesn't exist).
 *
 * @author lroellin
 */
public class PayerInactiveException extends PersonInactiveException {

    private static final long serialVersionUID = 1L;

    public PayerInactiveException() {
        super("PAYER_INACTIVE");
    }

}
