package io.teiler.server.util.exceptions;

/**
 * Exception denoting that a payer and a profiteer somehow clash.
 *
 * @author lroellin
 */
public class PayerProfiteerConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PayerProfiteerConflictException() {
        super("PAYER_AND_ROFITEER_CONFLICT");
    }

}
