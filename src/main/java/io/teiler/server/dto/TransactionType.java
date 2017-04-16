package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

/**
 * The different types of Transactions.
 * 
 * @author pbaechli
 */
public enum TransactionType {
    @SerializedName("expense")
    expense,
    @SerializedName("compensation")
    COMPENSATION,
}
