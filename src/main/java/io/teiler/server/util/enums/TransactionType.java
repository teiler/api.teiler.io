package io.teiler.server.util.enums;

import com.google.gson.annotations.SerializedName;

/**
 * The different types of Transactions.
 *
 * @author pbaechli
 */
public enum TransactionType {
    @SerializedName("EXPENSE")
    EXPENSE,
    @SerializedName("COMPENSATION")
    COMPENSATION,
}
