package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about a debt.
 *
 *  @author dthoma
 */
public class Debt {

    @SerializedName("person")
    private Person person;

    @SerializedName("balance")
    private Integer balance;

    public Debt(Person person, Integer balance) {
        this.person = person;
        this.balance = balance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
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
        return "Debt [person=" + person.toString() + ", balance=" + balance + "]";
    }
}
