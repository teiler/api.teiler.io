package io.teiler.server.services;

import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.persistence.entities.ExpenseEntity;
import io.teiler.server.persistence.entities.ProfiteerEntity;
import io.teiler.server.persistence.repositories.ExpenseRepository;
import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.services.util.ExpenseUtil;
import io.teiler.server.services.util.GroupUtil;
import io.teiler.server.services.util.TransactionUtil;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides service-methods for Expenses.
 *
 * @author pbaechli
 */
@Service
public class ExpenseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseService.class);

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ProfiteerRepository profiteerRepository;

    @Autowired
    private GroupUtil groupUtil;

    @Autowired
    private ExpenseUtil expenseUtil;

    @Autowired
    private TransactionUtil transactionUtil;

    /**
     * Creates a new Expense.
     *
     * @param expense {@link List} of {@link Profiteer} related to the Expense
     * @return Information about the Expense
     */
    public Expense createExpense(Expense expense, String groupId) {
        groupUtil.checkIdExists(groupId);
        expenseUtil.checkValuesGreaterThanZero(expense);
        transactionUtil.checkPayerBelongsToThisGroup(groupId, expense.getPayer().getId());
        expenseUtil.checkSharesAddUp(expense.getAmount(), expense.getProfiteers());
        transactionUtil.checkPayerIsActive(expense.getPayer().getId());

        // Before we create anything, let's check all the profiteers
        for (Profiteer share : expense.getProfiteers()) {
            transactionUtil.checkProfiteerIsActive(share.getPerson().getId());
            transactionUtil.checkProfiteerBelongsToThisGroup(groupId, share.getPerson().getId());
        }

        ExpenseEntity expenseEntity = expenseRepository.create(expense);

        for (Profiteer share : expense.getProfiteers()) {
            share.setTransactionId(expenseEntity.getId());
            profiteerRepository.create(share);
        }

        LOGGER.debug("Create expense: {}", expense);
        return expenseRepository.getById(expenseEntity.getId()).toExpense();
    }

    /**
     * Returns an {@link Expense} with the given Id and Group-Id.
     *
     * @param groupId Id of the Group
     * @param expenseId Id of the Expense
     * @return {@link List} of {@link Expense}
     */
    public Expense getExpense(String groupId, int expenseId) {
        groupUtil.checkIdExists(groupId);
        expenseUtil.checkExpenseExists(expenseId);
        expenseUtil.checkExpenseBelongsToThisGroup(groupId, expenseId);

        ExpenseEntity expense = expenseRepository.getByGroupIdAndExpenseId(groupId, expenseId);
        LOGGER.debug("View expense: {}", expense);
        return expense.toExpense();
    }

    /**
     * Returns a {@link List} of {@link Expense} in the Group with the given Id
     * sorted descending by the <code>update-time</code>.<br>
     * <i>Note:</i> The Group of the Payer of the Expense has to to match the given Group.
     *
     * @param groupId Id of the Group
     * @param limit Maximum amount of Expenses to fetch
     * @return {@link List} of {@link Expense}
     */
    public List<Expense> getLastExpenses(String groupId, long limit) {
        groupUtil.checkIdExists(groupId);

        List<ExpenseEntity> expenses = expenseRepository.getExpensesByGroupIdAndOrderedByUpdateTimeDesc(groupId, limit);
        LOGGER.debug("Get last expenses: {}, limit: {}", expenses, limit);
        return expenses.stream().map(ExpenseEntity::toExpense).collect(Collectors.toList());
    }

    /**
     * Updates and already created Expense with the given values.<br>
     * <i>Note:</i> The Expense has to exist within the given Group.
     *
     * @param groupId Id of the Group
     * @param expenseId Id of the Expense
     * @param changedExpense {@link Expense} containing the new values
     * @return {@link Expense} containing the new values
     */
    public Expense editExpense(String groupId, int expenseId, Expense changedExpense) {
        groupUtil.checkIdExists(groupId);
        expenseUtil.checkValuesGreaterThanZero(changedExpense);
        expenseUtil.checkExpenseExists(expenseId);
        expenseUtil.checkExpenseBelongsToThisGroup(groupId, expenseId);
        transactionUtil.checkPayerBelongsToThisGroup(groupId, changedExpense.getPayer().getId());
        expenseUtil.checkSharesAddUp(changedExpense.getAmount(), changedExpense.getProfiteers());

        // Before we create anything, let's check all the profiteers
        changedExpense.getProfiteers()
            .forEach(p -> transactionUtil.checkProfiteerBelongsToThisGroup(groupId, p.getPerson().getId()));

        LOGGER.debug("Edit expense: {}", changedExpense);
        expenseRepository.editExpense(expenseId, changedExpense);
        ExpenseEntity expenseEntity = expenseRepository.getById(expenseId);

        List<Integer> removedProfiteerPersonIds = expenseEntity.getProfiteers().stream().map(p -> p.getPerson().getId())
            .collect(Collectors.toList());

        for (Profiteer changedShare : changedExpense.getProfiteers()) {
            try {
                transactionUtil.checkProfiteerExistsInThisTransaction(expenseEntity.getId(),
                    changedShare.getPerson().getId());

                // does exist and was not removed => update the existing one
                LOGGER.debug("-- Updating Profiteer: {}", changedShare);
                ProfiteerEntity profiteerEntity = profiteerRepository
                    .getByTransactionIdAndProfiteerPersonId(expenseEntity.getId(), changedShare.getPerson().getId());
                profiteerRepository.editProfiteer(profiteerEntity.getId(), changedShare);

                // remove this profiteer from the list of removed profiteers as it has not been removed from the expense
                removedProfiteerPersonIds.remove(changedShare.getPerson().getId());
            } catch (ProfiteerNotFoundException e) {
                LOGGER.debug("-- Adding Profiteer: {}", changedShare);
                // does not yet exist => create a new one
                changedShare.setTransactionId(expenseEntity.getId());
                profiteerRepository.create(changedShare);
            }
        }

        // remove all the remaining profiteers as they were not included in the input and thus shall be removed
        LOGGER.debug("-- Deleting Profiteers: {}", removedProfiteerPersonIds);
        profiteerRepository
            .deleteProfiteerByTransactionIdAndProfiteerPersonIdList(expenseEntity.getId(), removedProfiteerPersonIds);

        return expenseRepository.getById(expenseEntity.getId()).toExpense();
    }

    /**
     * Deletes the Expense with the given Id and Group-Id.<br>
     * <i>Note:</i> The Expense has to exist within the given Group.
     *
     * @param groupId Id of the Group
     * @param expenseId Id of the Expense
     */
    public void deleteExpense(String groupId, int expenseId) {
        groupUtil.checkIdExists(groupId);
        expenseUtil.checkExpenseExists(expenseId);
        expenseUtil.checkExpenseBelongsToThisGroup(groupId, expenseId);
        LOGGER.debug("Delete expense: {}", expenseId);
        expenseRepository.deleteExpense(expenseId);
    }

}
