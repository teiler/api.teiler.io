package io.teiler.server.persistence.entities;

import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import io.teiler.server.dto.Expense;
import io.teiler.server.dto.TransactionType;
import io.teiler.server.util.TimeUtil;

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

    public ExpenseEntity(Expense expense) {
        super.setId(expense.getId());
        super.setPayer(new PersonEntity(expense.getPayer()));
        super.setUpdateTime(TimeUtil.convertToTimestamp(expense.getUpdateTime()));
        super.setCreateTime(TimeUtil.convertToTimestamp(expense.getCreateTime()));
        super.setTransactionType(TransactionType.EXPENSE);

        this.title = expense.getTitle();
    }

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

}
