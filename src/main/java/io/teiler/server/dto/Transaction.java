package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

/**
 * Class holding all information about a transaction.
 *
 * @author pbaechli
 */
public abstract class Transaction {

    @SerializedName("id")
    private Integer id;

    @SerializedName("amount")
    private Integer amount;

    @SerializedName("payer")
    private Person payer;

    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * @param id Id of the Transaction
     * @param amount Amount of money involved in the transaction
     * @param payer Person who spent the money
     * @param updateTime {@link LocalDateTime} marking the last update of the Transaction
     * @param createTime {@link LocalDateTime} marking the creation of the Transaction
     */
    public Transaction(Integer id, Integer amount, Person payer, LocalDateTime updateTime,
        LocalDateTime createTime) {
        this.id = id;
        this.amount = amount;
        this.payer = payer;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", amount=" + amount + ", payer=" + payer + ", updateTime="
            + updateTime + ", createTime=" + createTime + "]";
    }

}
