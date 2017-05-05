package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Debt;
import io.teiler.server.persistence.repositories.PersonRepository;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Entity representing a virtual debt entry.
 *
 * @author dthoma
 */
public class DebtEntity {

    @Column(name = "person")
    private Integer personID;

    @Column(name = "balance")
    private Integer balance;

    public DebtEntity() { /* intentionally empty */ }

    /**
     * Converts a {@link Debt} to a {@link DebtEntity}
     *
     * @param debt
     */
    public DebtEntity(Debt debt) {
        this.personID = debt.getPerson();
        this.balance = debt.getBalance();
    }

    public Debt toDebt() {
        return new Debt(this.personID, this.balance);
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return this.toDebt().toString();
    }
}
