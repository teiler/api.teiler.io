package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Expense;
import io.teiler.server.util.TimeUtil;
import io.teiler.server.util.enums.TransactionType;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Entity representing an Expense-entry of the <code>transaction</code>-table.
 *
 * @author pbaechli
 */
@Entity
@DiscriminatorValue("EXPENSE")
public class ExpenseEntity extends TransactionEntity {

    @NotNull
    @Column(name = "title")
    private String title;

    public ExpenseEntity() { /* intentionally empty */ }

    /**
     * Creates an ExpenseEntity from a DTO expense.
     *
     * @param expense The DTO expense
     */
    public ExpenseEntity(Expense expense) {
        super.setId(expense.getId());
        super.setPayer(new PersonEntity(expense.getPayer()));
        super.setUpdateTime(TimeUtil.convertToTimestamp(expense.getUpdateTime()));
        super.setCreateTime(TimeUtil.convertToTimestamp(expense.getCreateTime()));
        super.setTransactionType(TransactionType.EXPENSE);

        this.title = expense.getTitle();
    }

    /**
     * Returns itself as a DTO expense.
     *
     * @return itself as a DTO expense
     */
    public Expense toExpense() {
        return new Expense(getId(), getAmount(), getPayer().toPerson(),
            TimeUtil.convertToLocalDateTime(getUpdateTime()),
            TimeUtil.convertToLocalDateTime(getCreateTime()), title,
            getProfiteers().stream().map(ProfiteerEntity::toProfiteer).collect(Collectors.toList()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.toExpense().toString();
    }

}
