package io.teiler.server.util.exceptions;

/**
 * Exception denoting that access to some resource(s) has not been authorised.
 *
 * @author lroellin
 */
public class PersonHasUnsettledDebtsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersonHasUnsettledDebtsException() {
        super("PERSON_HAS_UNSETTLED_DEBTS");
    }

}
