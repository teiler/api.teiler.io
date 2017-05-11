package io.teiler.server.services;

import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Debt;
import io.teiler.server.services.util.GroupUtil;
import io.teiler.server.services.util.settleup.PersonChooser;
import io.teiler.server.services.util.settleup.TopBottomChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides service-methods for suggested compensations.
 *
 * @author dthoma
 */
@Service
public class SuggestCompensationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestCompensationService.class);

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

            if (creditor.getBalance() >= debitor.getBalance()) {
                balance = -debitor.getBalance();

                newCreditorBalance = creditor.getBalance() + debitor.getBalance();
                newDebitorBalance = 0;
            } else {
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

            personChooser.updateDebts(newCreditorBalance, creditor);
            personChooser.updateDebts(newDebitorBalance, debitor);
        }

        LOGGER.debug("View suggested compensations: {}", suggestedCompensations);
        return suggestedCompensations;
    }
}
