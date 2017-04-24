package io.teiler.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Share;
import io.teiler.server.persistence.repositories.ExpenseRepository;
import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.util.exceptions.FactorsNotAddingUpException;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;

@Service
public class ExpenseUtil {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ProfiteerRepository profiteerRepository;

    public ExpenseUtil() { /* intentionally empty */ }

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
     * @param expenseId Id of the Expense
     * @throws TransactionNotFoundException Expense does not exist
     */
    public void checkExpenseExists(int expenseId) throws TransactionNotFoundException {
        if (expenseRepository.getById(expenseId) == null) {
            throw new TransactionNotFoundException();
        }
    }
    
    /**
     * Checks whether a Profiteer-Person exists within an Expense.
     * 
     * @param expenseId Id of the Expense
     * @param profiteerPersonId Id of the Profiteer-Person
     * @throws ProfiteerNotFoundException Profiteer does not exist
     */
    public void checkProfiteerExistsInThisExpense(int expenseId, int profiteerPersonId) throws ProfiteerNotFoundException {
        if (profiteerRepository.getByExpenseIdAndProfiteerPersonId(expenseId, profiteerPersonId) == null) {
            throw new ProfiteerNotFoundException();
        }
    }
    
    /**
     * Checks whether the factors of the given shares add up to 1.0.
     * 
     * @param shares {@link List} of {@link Share}
     * @throws FactorsNotAddingUpException Factors do not add up
     */
    public void checkFactorsAddUp(List<Share> shares) throws FactorsNotAddingUpException {
        double total = shares.stream().map(Share::getShare).mapToInt(Integer::intValue).sum();
        if (total != 1.0) {
            throw new FactorsNotAddingUpException();
        }
    }
    
}
