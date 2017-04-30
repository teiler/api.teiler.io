package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a payer and a profiteer somehow clash.
 *
 * @author lroellin
 */
public class PersonInactiveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersonInactiveException() {
        super("PERSON_INACTIVE");
    }

    public PersonInactiveException(String message) {
        super(message);
    }

}
