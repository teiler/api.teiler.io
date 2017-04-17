package io.teiler.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.persistence.repositories.ExpenseRepository;
import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;

@Service
public class ExpenseUtil {
    
    public ExpenseUtil() { /* intentionally empty */ }

    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private ProfiteerRepository profiteerRepository;

    /**
     * Checks whether an Expense exists within a Group.
     * 
     * @param groupId Id of the Group
     * @param expenseId Id of the Expense
     * @throws TransactionNotFoundException Expense does not exists within Group
     */
    public void checkExpenseBelongsToThisGroup(String groupId, int expenseId) throws TransactionNotFoundException {
        if (expenseRepository.getByGroupIdAndExpenseId(groupId, expenseId) == null) {
            throw new TransactionNotFoundException();
        }
    }

    /**
     * Checks whether an Expense exists.
     * 
     * @param personId Id of the Expense
     * @throws TransactionNotFoundException Expense does not exist
     */
    public void checkExpenseExists(int expenseId) throws TransactionNotFoundException {
        if (expenseRepository.getById(expenseId) == null) {
            throw new TransactionNotFoundException();
        }
    }
    
    public void checkProfiteerExistsInThisExpense(int expenseId, int profiteerId) {
        if (profiteerRepository.getByExpenseIdAndProfiteerPersonId(expenseId, profiteerId) == null) {
            throw new ProfiteerNotFoundException();
        }
    }
    
}
