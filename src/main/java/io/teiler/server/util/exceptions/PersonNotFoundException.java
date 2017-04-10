package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a person has not been found (and presumably doesn't exist).
 * 
 * @author lroellin
 */
public class PersonNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public PersonNotFoundException() {
        super("PERSON_NOT_FOUND");
    }
    
}
