package io.teiler.server.dto;

import java.time.LocalDateTime;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the share a Person takes in a Transaction.
 * 
 * @author pbaechli
 */
public class Profiteer {

    private transient Integer transactionId;

    @SerializedName("person")
    private Person person;
    
    @SerializedName("share")
    private Integer share;

    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * Usual constructor.
     * 
     * @param transactionId Id of the Transaction in which share is being taken
     * @param person Person taking share in the Transaction
     * @param share Amount of money by which share is being taken
     */
    public Profiteer(Integer transactionId, Person person, Integer share) {
        this.transactionId = transactionId;
        this.person = person;
        this.share = share;
    }
    
    /**
     * Constructor with all fields (useful for converting).
     * 
     * @param transactionId Id of the Transaction in which share is being taken
     * @param person Person taking share in the Transaction
     * @param share Amount of money by which share is being taken
     * @param updateTime {@link LocalDateTime} marking the last update of the Share
     * @param createTime {@link LocalDateTime} marking the creation of the Share
     */
    public Profiteer(Integer transactionId, Person person, Integer share, LocalDateTime updateTime,
            LocalDateTime createTime) {
        this.transactionId = transactionId;
        this.person = person;
        this.share = share;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
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
        return "Profiteer [person=" + person + ", share=" + share + ", updateTime=" + updateTime
                + ", createTime=" + createTime + ", transactionId(transient)=" + transactionId + "]";
    }
    
}
