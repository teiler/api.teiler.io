package io.teiler.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Share;
import io.teiler.server.persistence.entities.ExpenseEntity;
import io.teiler.server.persistence.repositories.ExpenseRepository;
import io.teiler.server.persistence.repositories.ProfiteerRepository;

/**
 * Provides service-methods for Expenses.
 * 
 * @author pbaechli
 */
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private ProfiteerRepository profiteerRepository;
    
    /**
     * Creates a new Expense.
     * @param expense {@link List} of {@link Share} related to the Expense
     * 
     * @return Information about the Expense
     */
    public Expense createExpense(Expense expense) {
        ExpenseEntity expenseEntity = expenseRepository.create(expense);
        
        for(Share share : expense.getShares()) {
            share.setExpenseId(expenseEntity.getId());
            profiteerRepository.create(share);
        }
        
        return expenseRepository.getById(expenseEntity.getId()).toExpense();
    }
    
}
