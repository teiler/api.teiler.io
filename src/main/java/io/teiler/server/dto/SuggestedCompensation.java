package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about a suggested payment.
 *
 * @author dthoma
 */
public class SuggestedCompensation {

    @SerializedName("amount")
    private Integer amount;

    @SerializedName("payer")
    private Person payer;

    @SerializedName("profiteer")
    private Person profiteer;

    /**
     * Creates a suggested compensations.
     *
     * @param amount How much this compensation is about
     * @param payer Who should pay in this compensation
     * @param profiteer Who should receive in this compensation
     */
    public SuggestedCompensation(Integer amount, Person payer, Person profiteer) {
        this.amount = amount;
        this.payer = payer;
        this.profiteer = profiteer;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Person getPayer() {
        return payer;
    }

    public void setPayer(Person payer) {
        this.payer = payer;
    }

    public Person getProfiteer() {
        return profiteer;
    }

    public void setProfiteer(Person profiteer) {
        this.profiteer = profiteer;
    }

    @Override
    public String toString() {
        return "SuggestedCompensation [amount=" + amount + ", payer=" + payer.toString() + ", profiteer=" + profiteer
            .toString() + "]";
    }
}
