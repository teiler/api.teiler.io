package io.teiler.server.util.exceptions;

/**
 * Exception denoting that person names in a group clash
 * 
 * @author lroellin
 */
public class PeopleNameConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PeopleNameConflictException() {
        super("PEOPLE_NAME_CONFLICT");
    }
}
