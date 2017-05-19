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
package io.teiler.server.services.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.persistence.repositories.ExpenseRepository;
import io.teiler.server.util.exceptions.SharesNotAddingUpException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;
import io.teiler.server.util.exceptions.ValueLessThanOrEqualToZeroException;

@Service
public class ExpenseUtil {

    @Autowired
    private ExpenseRepository expenseRepository;

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
     * Checks whether the given shares add up to the expected amount.
     *
     * @param expectedTotalAmount Expected amount of the summed up shares
     * @param shares {@link List} of {@link Profiteer}
     * @throws SharesNotAddingUpException Shares do not add up
     */
    public void checkSharesAddUp(Integer expectedTotalAmount, List<Profiteer> shares)
        throws SharesNotAddingUpException {
        Integer total = shares.stream().map(Profiteer::getShare).mapToInt(Integer::intValue).sum();
        if (total.compareTo(expectedTotalAmount) != 0) {
            throw new SharesNotAddingUpException();
        }
    }

    /**
     * Checks whether the values are greater than zero.
     *
     * @param expense The compensation to check
     * @throws ValueLessThanOrEqualToZeroException Values less than or equal to zero
     */
    public void checkValuesGreaterThanZero(Expense expense)
        throws ValueLessThanOrEqualToZeroException {
        if (expense.getAmount() <= 0 || expense.getProfiteers().stream().anyMatch(p -> p.getShare() <= 0)) {
            throw new ValueLessThanOrEqualToZeroException();
        }
    }

}
