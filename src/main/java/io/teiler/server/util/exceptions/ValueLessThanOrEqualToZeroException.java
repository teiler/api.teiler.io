package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a value has been less than or equal to zero.
 *
 * @author dthoma
 */
public class ValueLessThanOrEqualToZeroException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValueLessThanOrEqualToZeroException() {
        super("VALUE_LESS_THAN_OR_EQUAL_TO_ZERO");
    }

}
