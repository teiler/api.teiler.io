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
package io.teiler.server.persistence.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Expense;
import io.teiler.server.persistence.entities.ExpenseEntity;
import io.teiler.server.persistence.entities.QExpenseEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Provides database-related operations for Expenses.
 *
 * @author pbaechli
 */
@Repository
public class ExpenseRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Creates a new {@link ExpenseEntity} and returns it.
     *
     * @param expense {@link Expense}
     * @return {@link ExpenseEntity}
     */
    @Transactional
    public ExpenseEntity create(Expense expense) {
        ExpenseEntity expenseEntity = new ExpenseEntity(expense);
        entityManager.persist(expenseEntity);
        return expenseEntity;
    }

    /**
     * Returns a {@link ExpenseEntity} with the Id.
     *
     * @param id Id of the Expense
     * @return {@link ExpenseEntity}
     */
    public ExpenseEntity getById(int id) {
        return new JPAQuery<ExpenseEntity>(entityManager).from(QExpenseEntity.expenseEntity)
            .where(QExpenseEntity.expenseEntity.id.eq(id))
            .fetchOne();
    }

    /**
     * Returns an {@link ExpenseEntity} with the given Id and Group-Id.<br>
     * <i>Note:</i> The Group of the Payer of the Expense has to to match the given Group.
     *
     * @param groupId Id of the Group of the Payer
     * @param expenseId Id of the Expense
     * @return {@link ExpenseEntity}
     */
    public ExpenseEntity getByGroupIdAndExpenseId(String groupId, int expenseId) {
        return new JPAQuery<ExpenseEntity>(entityManager).from(QExpenseEntity.expenseEntity)
            .where(QExpenseEntity.expenseEntity.id.eq(expenseId))
            .where(QExpenseEntity.expenseEntity.payer.groupId.eq(groupId))
            .fetchOne();
    }

    /**
     * Returns a {@link List} of {@link ExpenseEntity} in the Group with the given Id
     * ordered by <code>update-time</code> descending.<br>
     * <i>Note:</i> The Expense has to exist within the given Group.
     *
     * @param groupId Id of the Group
     * @param limit Maximum amount of Expenses to fetch
     * @return {@link List} of {@link ExpenseEntity}
     */
    public List<ExpenseEntity> getExpensesByGroupIdAndOrderedByUpdateTimeDesc(String groupId, long limit) {
        return new JPAQuery<ExpenseEntity>(entityManager).from(QExpenseEntity.expenseEntity)
            .where(QExpenseEntity.expenseEntity.payer.groupId.eq(groupId))
            .where(QExpenseEntity.expenseEntity.profiteers.any().person.groupId.eq(groupId))
            .orderBy(QExpenseEntity.expenseEntity.updateTime.desc())
            .limit(limit)
            .fetch();
    }

    /**
     * Updates a already persisted {@link ExpenseEntity} with the given values.
     *
     * @param expenseId Id of the Expense
     * @param changedExpense {@link Expense} containing the new values
     * @return {@link ExpenseEntity} containing the new values
     */
    @Transactional
    public ExpenseEntity editExpense(int expenseId, Expense changedExpense) {
        ExpenseEntity expense = new ExpenseEntity(changedExpense);
        expense.setId(expenseId);

        entityManager.merge(expense);

        return expense;
    }

    /**
     * Deletes the {@link ExpenseEntity} with the given Id.
     *
     * @param expenseId Id of the Expense
     */
    @Transactional
    public void deleteExpense(int expenseId) {
        ExpenseEntity expense = getById(expenseId);
        entityManager.remove(expense);
    }

}
