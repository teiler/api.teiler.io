package io.teiler.server.persistence.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import io.teiler.server.dto.Expense;
import io.teiler.server.persistence.entities.ExpenseEntity;
import io.teiler.server.persistence.entities.QExpenseEntity;

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
     * Returns an {@link ExpenseEntity} with the given Id and Group-Id.
     * <i>Note:</i> The Group of the Payer of the Expense has to to match the given Group.
     * 
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
     * Returns a {@link List} of {@link ExpenseEntity} in the Group with the given Id.
     * <i>Note:</i> The Expense has to exist within the given Group.
     * 
     * @param groupId Id of the Group
     * @param limit Maximum amount of Expenses to fetch
     * @return {@link List} of {@link ExpenseEntity}
     */
    public List<ExpenseEntity> getExpensesByGroupId(String groupId, long limit) {
        return new JPAQuery<ExpenseEntity>(entityManager).from(QExpenseEntity.expenseEntity)
            .where(QExpenseEntity.expenseEntity.payer.groupId.eq(groupId))
            .where(QExpenseEntity.expenseEntity.profiteers.any().person.groupId.eq(groupId))
            .limit(limit)
            .orderBy(QExpenseEntity.expenseEntity.id.asc())
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
