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
package io.teiler.server.services;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Debt;
import io.teiler.server.services.util.GroupUtil;
import io.teiler.server.services.util.settleup.PersonChooser;
import io.teiler.server.services.util.settleup.TopBottomChooser;

/**
 * Provides service-methods for suggested compensations.
 *
 * @author dthoma
 */
@Service
public class SuggestedCompensationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestedCompensationsService.class);

    @Autowired
    private GroupUtil groupUtil;

    @Autowired
    private DebtService debtService;

    /**
     * Gets the suggested compensations for this group.
     *
     * @param groupId The group to get the suggested compensations for.
     * @return The suggested compensations for this group
     */
    public List<Compensation> getSuggestedCompensations(String groupId) {
        groupUtil.checkIdExists(groupId);

        List<Compensation> suggestedCompensations = new LinkedList<>();
        PersonChooser personChooser = new TopBottomChooser(debtService.getDebts(groupId));

        while (personChooser.personsLeft()) {
            Debt creditor = personChooser.getNextCreditor();
            Debt debitor = personChooser.getNextDebitor();

            int newCreditorBalance;
            int newDebitorBalance;
            int balance;
            
            // The idea is to eliminate (balance = 0) a creditor or a debitor each loop
            if (creditor.getBalance() >= -debitor.getBalance()) {
                /*
                The creditor's credit is higher than (or equal to) the debitor's debt
                Therefore, we can eliminate the debitor
                The debitor will pay his whole debt to the creditor
                */
                balance = -debitor.getBalance();

                newCreditorBalance = creditor.getBalance() + debitor.getBalance();
                newDebitorBalance = 0;
            } else {
                /*
                The debitor's debt is higher than the creditor's credit
                Therefore, we can eliminate the creditor
                The debitor will pay the remaining balance of the creditor and ease his debt
                */
                balance = creditor.getBalance();

                newCreditorBalance = 0;
                newDebitorBalance = debitor.getBalance() + creditor.getBalance();
            }

            Compensation compensation = new Compensation(
                null,
                balance,
                debitor.getPerson(),
                creditor.getPerson()
            );
            suggestedCompensations.add(compensation);

            personChooser.updateDebt(newCreditorBalance, creditor);
            personChooser.updateDebt(newDebitorBalance, debitor);
        }

        LOGGER.debug("View suggested compensations: {}", suggestedCompensations);
        return suggestedCompensations;
    }
    
}
