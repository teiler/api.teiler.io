package io.teiler.server.util.exceptions;

/**
 * Exception denoting that access to some resource(s) has not been authorised.
 *
 * @author lroellin
 */
public class NotAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotAuthorizedException() {
        super("NOT_AUTHORIZED_TO_GROUP");
    }

}
