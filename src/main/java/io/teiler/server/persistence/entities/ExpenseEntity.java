/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
