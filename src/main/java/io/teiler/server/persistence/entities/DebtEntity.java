package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Debt;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

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
    private Integer personId;

    @Column(name = "balance")
    private Integer balance;

    public DebtEntity() { /* intentionally empty */ }

    /**
     * Converts a {@link Debt} to a {@link DebtEntity}.
     */
    public DebtEntity(Debt debt) {
        this.personId = debt.getPerson().getId();
        this.balance = debt.getBalance();
    }

    public DebtEntity(Integer person, Integer balance) {
        this.personId = person;
        this.balance = balance;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "DebtEntity [person=" + personId + ", balance=" + balance + "]";
    }

}
