package io.teiler.server.dto;

import java.time.LocalDateTime;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the share a Person takes in an Expense.
 * 
 * @author pbaechli
 */
public class Profiteer {

    private transient Integer expenseId;

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
     * @param expenseId Id of the Expense in which share is being taken
     * @param person Person taking share in the Expense
     * @param factor Factor by which share is being taken
     */
    public Profiteer(Integer expenseId, Person person, Integer share) {
        this.expenseId = expenseId;
        this.person = person;
        this.share = share;
    }
    
    /**
     * Constructor with all fields (useful for converting).
     * 
     * @param expense Id of the Expense in which share is being taken
     * @param person Person taking share in the Expense
     * @param factor Factor by which share is being taken
     * @param updateTime {@link LocalDateTime} marking the last update of the Share
     * @param createTime {@link LocalDateTime} marking the creation of the Share
     */
    public Profiteer(Integer expenseId, Person person, Integer share, LocalDateTime updateTime,
            LocalDateTime createTime) {
        this.expenseId = expenseId;
        this.person = person;
        this.share = share;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer transactionId) {
        this.expenseId = transactionId;
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
                + ", createTime=" + createTime + ", expenseId(transient)=" + expenseId + "]";
    }
    
}
