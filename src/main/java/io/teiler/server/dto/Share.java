package io.teiler.server.dto;

import java.time.LocalDateTime;

import com.google.gson.annotations.SerializedName;

/**
 * <br> 
 * Represents the share a Person takes in an Expense.
 * 
 * @author pbaechli
 */
public class Share {

    private transient Integer expenseId;

    @SerializedName("profiteer")
    private Person profiteer;
    
    @SerializedName("factor")
    private Double factor;

    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * Usual constructor.
     * 
     * @param expenseId Id of the Expense in which share is being taken
     * @param profiteer Person taking share in the Expense
     * @param factor Factor by which share is being taken
     */
    public Share(Integer expenseId, Person profiteer, Double factor) {
        this.expenseId = expenseId;
        this.profiteer = profiteer;
        this.factor = factor;
    }
    
    /**
     * Constructor with all fields (useful for converting).
     * 
     * @param expense Id of the Expense in which share is being taken
     * @param profiteer Person taking share in the Expense
     * @param factor Factor by which share is being taken
     * @param updateTime {@link LocalDateTime} marking the last update of the Share
     * @param createTime {@link LocalDateTime} marking the creation of the Share
     */
    public Share(Integer expenseId, Person profiteer, Double factor, LocalDateTime updateTime,
            LocalDateTime createTime) {
        this.expenseId = expenseId;
        this.profiteer = profiteer;
        this.factor = factor;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Person getProfiteer() {
        return profiteer;
    }

    public void setProfiteer(Person profiteer) {
        this.profiteer = profiteer;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
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
        return "Share [profiteer=" + profiteer + ", factor=" + factor + ", updateTime=" + updateTime
                + ", createTime=" + createTime + ", expenseId(transient)=" + expenseId + "]";
    }
    
}
