package io.teiler.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Person;
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
     * 
     * @param title Title of the Expense
     * @param amount Amount of money involved in the Expense
     * @param payer Person who spent the money
     * @param shares {@link List} of {@link Share} related to the Expense
     * @return Information about the Expense
     */
    public Expense createExpense(String title, int amount, Person payer, List<Share> shares) {
        Expense expense = new Expense(null, amount, payer, title, shares);
        
        ExpenseEntity expenseEntity = expenseRepository.create(expense);
        
        for(Share share : shares) {
            share.setExpenseId(expenseEntity.getId());
            profiteerRepository.create(share);
        }
        
        return expenseRepository.getById(expenseEntity.getId()).toExpense();
    }
    
}
