package io.teiler.server.dto;

import java.time.LocalDateTime;

import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about a Compensation.
 * 
 * @author pbaechli
 */
public class Compensation extends Transaction {

    @SerializedName("profiteer")
    private Person profiteer;

    /**
     * Usual constructor.
     * 
     * @param id Id of the Compensation
     * @param amount Amount of money involved in the Compensation
     * @param payer Person who spent the money
     * @param profiteer {@link Profiteer} related to the Compensation
     */
    public Compensation(Integer id, Integer amount, Person payer, Person profiteer) {
        super(id, amount, payer, null, null);
        this.profiteer = profiteer;
    }

    /**
     * Constructor with all fields (useful for converting).
     * 
     * @param id Id of the Compensation
     * @param amount Amount of money involved in the Compensation
     * @param payer Person who spent the money
     * @param updateTime {@link LocalDateTime} marking the last update of the Compensation
     * @param createTime {@link LocalDateTime} marking the creation of the Compensation
     * @param profiteer {@link Profiteer} related to the Compensation
     */
    public Compensation(Integer id, Integer amount, Person payer, LocalDateTime updateTime,
            LocalDateTime createTime, Person profiteer) {
        super(id, amount, payer, updateTime, createTime);
        this.profiteer = profiteer;
    }

    public Person getProfiteer() {
        return profiteer;
    }

    public void setProfiteer(Person profiteer) {
        this.profiteer = profiteer;
    }

    @Override
    public String toString() {
        return "Compensation [profiteer=" + profiteer + "]";
    }

}
