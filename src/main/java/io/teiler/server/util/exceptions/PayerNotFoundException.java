package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a person has not been found (and presumably doesn't exist).
 *
 * @author lroellin
 */
public class PayerNotFoundException extends PersonNotFoundException {

    private static final long serialVersionUID = 1L;

    public PayerNotFoundException() {
        super("PAYER_NOT_FOUND");
    }

}
