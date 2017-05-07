package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Debt;
import io.teiler.server.persistence.repositories.PersonRepository;

import javax.persistence.*;

/**
 * Entity representing a virtual debt entry.
 *
 * @author dthoma
 */
@Entity
@SqlResultSetMapping(name = "GET_DEBT_QUERY",
        classes = {
                @ConstructorResult(targetClass = io.teiler.server.persistence.entities.DebtEntity.class,
                        columns = {
                                @ColumnResult(name = "person", type = Integer.class),
                                @ColumnResult(name = "balance", type = Integer.class)})
        })
public class DebtEntity {

    @Id
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
        this.personID = debt.getPerson().getId();
        this.balance = debt.getBalance();
    }

    public DebtEntity(Integer person, Integer balance) {
        this.personID = person;
        this.balance = balance;
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
        return "DebtEntity [person=" + personID + ", balance=" + balance + "]";
    }

}
