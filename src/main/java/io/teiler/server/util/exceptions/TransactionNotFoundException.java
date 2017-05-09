package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a Transaction has not been found (and presumably doesn't exist).
 *
 * @author pbaechli
 */
public class TransactionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TransactionNotFoundException() {
        super("TRANSACTION_NOT_FOUND");
    }

}
