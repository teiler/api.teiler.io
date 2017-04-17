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
     * The Id of the {@link ExpenseEntity} and the Group-Id of the Payer are to match exactly.
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
     * Deletes the Expense with the given Id.
     * 
     * @param expenseId Id of the Expense
     */
    @Transactional
    public void deleteExpense(int expenseId) {
        ExpenseEntity expense = getById(expenseId);
        entityManager.remove(expense);
    }
    
}
