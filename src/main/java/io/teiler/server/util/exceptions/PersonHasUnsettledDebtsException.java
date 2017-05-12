package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a person has one ore more unsettled debts.
 *
 * @author lroellin
 */
public class PersonHasUnsettledDebtsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersonHasUnsettledDebtsException() {
        super("PERSON_HAS_UNSETTLED_DEBTS");
    }

}
