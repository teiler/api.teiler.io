package io.teiler.server.persistence.entities;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
@DiscriminatorValue("expense")
public class ExpenseEntity extends TransactionEntity {

    @NotNull
    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "expenseId", fetch = FetchType.EAGER, orphanRemoval = false)
    private List<ProfiteerEntity> profiteers;

    public ExpenseEntity() { /* intentionally empty */ }

    public ExpenseEntity(Expense expense) {
        super.setId(expense.getId());
        super.setAmount(expense.getAmount());
        super.setPayer(new PersonEntity(expense.getPayer()));
        super.setUpdateTime(TimeUtil.convertToTimestamp(expense.getUpdateTime()));
        super.setCreateTime(TimeUtil.convertToTimestamp(expense.getCreateTime()));
        super.setTransactionType(TransactionType.expense);

        this.title = expense.getTitle();
    }

    public Expense toExpense() {
        return new Expense(getId(), getAmount(), getPayer().toPerson(),
                TimeUtil.convertToLocalDateTime(getUpdateTime()),
                TimeUtil.convertToLocalDateTime(getCreateTime()), title,
                profiteers.stream().map(p -> p.toShare()).collect(Collectors.toList()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProfiteerEntity> getProfiteers() {
        return profiteers;
    }

    public void setProfiteers(List<ProfiteerEntity> profiteers) {
        this.profiteers = profiteers;
    }

}
