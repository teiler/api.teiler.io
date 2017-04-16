package io.teiler.server.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about an expense.
 * 
 * @author pbaechli
 */
public class Expense extends Transaction {

    @SerializedName("title")
    private String title;

    @SerializedName("profiteers")
    private List<Share> shares;

    /**
     * Usual constructor.
     * 
     * @param id Id of the Expense
     * @param amount Amount of money involved in the Expense
     * @param payer Person who spent the money
     * @param title Title of the Expense
     * @param shares {@link List} of {@link Share} related to the Expense
     */
    public Expense(Integer id, Integer amount, Person payer, String title,
            List<Share> shares) {
        super(id, amount, payer, null, null);
        this.title = title;
        this.shares = shares;
    }

    /**
     * Constructor with all fields (useful for converting).
     * 
     * @param id Id of the Expense
     * @param amount Amount of money involved in the Expense
     * @param payer Person who spent the money
     * @param updateTime {@link LocalDateTime} marking the last update of the Expense
     * @param createTime {@link LocalDateTime} marking the creation of the Expense
     * @param title Title of the Expense
     * @param shares {@link List} of {@link Share} related to the Expense
     */
    public Expense(Integer id, Integer amount, Person payer, LocalDateTime updateTime,
            LocalDateTime createTime, String title, List<Share> shares) {
        super(id, amount, payer, updateTime, createTime);
        this.title = title;
        this.shares = shares;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Share> getShares() {
        return shares;
    }

    public void setShares(List<Share> shares) {
        this.shares = shares;
    }
    
}
