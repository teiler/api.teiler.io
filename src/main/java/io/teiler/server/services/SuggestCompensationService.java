package io.teiler.server.services;

import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Debt;
import io.teiler.server.services.util.GroupUtil;
import io.teiler.server.services.util.settleup.PersonChooser;
import io.teiler.server.services.util.settleup.TopBottomChooser;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides service-methods for suggested compensations.
 *
 * @author dthoma
 */
@Service
public class SuggestCompensationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestCompensationService.class);

    private SortedMap<Integer, Debt> debts;

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

        this.debts = new TreeMap<>();
        debtService.getDebts(groupId).stream().filter(d -> d.getBalance() != 0)
            .forEach(d -> this.debts.put(d.getBalance(), d));

        List<Compensation> suggestedCompensations = new LinkedList<>();
        PersonChooser personChooser = new TopBottomChooser(debts);

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

            updateDebts(newCreditorBalance, creditor);
            updateDebts(newDebitorBalance, debitor);
        }

        LOGGER.debug("View suggested compensations: {}", suggestedCompensations);
        return suggestedCompensations;
    }

    private void updateDebts(int newBalance, Debt debt) {
        this.debts.remove(debt.getBalance(), debt);
        debt.setBalance(newBalance);
        this.debts.put(newBalance, debt);
    }

}
