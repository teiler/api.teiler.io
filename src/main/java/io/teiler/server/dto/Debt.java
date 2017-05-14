package io.teiler.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class holding all information about a debt.
 *
 * @author dthoma
 */
public class Debt implements Comparable<Debt> {

    @Expose(deserialize = false)
    @SerializedName("person")
    private Person person;

    @Expose(deserialize = false)
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Debt debt = (Debt) o;

        if (person != null ? !person.equals(debt.person) : debt.person != null) {
            return false;
        }
        return balance != null ? balance.equals(debt.balance) : debt.balance == null;
    }

    @Override
    public int hashCode() {
        int result = person != null ? person.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(Debt that) {
        if (this.balance.compareTo(that.balance) > 0) {
            return -1;
        } else if (this.balance.compareTo(that.balance) < 0) {
            return 1;
        }
        return 0;
    }
}
