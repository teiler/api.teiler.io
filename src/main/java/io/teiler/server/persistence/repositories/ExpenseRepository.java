package io.teiler.server.persistence.repositories;

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
    public ExpenseEntity getById(Integer id) {
        return new JPAQuery<ExpenseEntity>(entityManager).from(QExpenseEntity.expenseEntity)
            .where(QExpenseEntity.expenseEntity.id.eq(id))
            .fetchOne();
    }
    
}
