package io.teiler.server.services;

import io.teiler.server.dto.Debt;
import io.teiler.server.dto.SuggestedCompensation;
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

    public List<SuggestedCompensation> getSuggestedCompensations(String groupID) {
        groupUtil.checkIdExists(groupID);

        this.debts = new TreeMap<>();
        debtService.getDebts(groupID).stream().filter(d -> d.getBalance() != 0)
            .forEach(d -> this.debts.put(d.getBalance(), d));

        List<SuggestedCompensation> suggestedCompensations = new LinkedList<>();
        PersonChooser personChooser = new TopBottomChooser(debts);

        while (personChooser.personsLeft()) {
            Debt creditor = personChooser.getNextCreditor();
            Debt debitor = personChooser.getNextDebitor();

            if (creditor.getBalance() >= debitor.getBalance()) {
                SuggestedCompensation compensation = new SuggestedCompensation(
                    -debitor.getBalance(),
                    debitor.getPerson(),
                    creditor.getPerson()
                );
                suggestedCompensations.add(compensation);

                int newCreditorBalance = creditor.getBalance() + debitor.getBalance();
                int newDebitorBalance = 0;

                updateDebts(newCreditorBalance, creditor);
                updateDebts(newDebitorBalance, debitor);
            } else {
                SuggestedCompensation compensation = new SuggestedCompensation(
                    creditor.getBalance(),
                    debitor.getPerson(),
                    creditor.getPerson()
                );
                suggestedCompensations.add(compensation);

                int newCreditorBalance = 0;
                int newDebitorBalance = debitor.getBalance() + creditor.getBalance();

                updateDebts(newCreditorBalance, creditor);
                updateDebts(newDebitorBalance, debitor);
            }
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
