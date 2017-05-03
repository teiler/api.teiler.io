package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about a debt.
 *
 *  @author dthoma
 */
public class Debt {

    @SerializedName("person")
    private int person;

    @SerializedName("balance")
    private int balance;

    public Debt(int person, int balance) {
        this.person = person;
        this.balance = balance;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Debt [person=" + person + ", balance=" + balance + "]";
    }
}
