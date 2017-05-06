package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about a debt.
 *
 *  @author dthoma
 */
public class Debt {

    @SerializedName("person")
    private Integer person;

    @SerializedName("balance")
    private Integer balance;

    public Debt(Integer person, Integer balance) {
        this.person = person;
        this.balance = balance;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Debt [person=" + person + ", balance=" + balance + "]";
    }
}
