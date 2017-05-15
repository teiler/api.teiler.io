package io.teiler.server.services.util.settleup;

import io.teiler.server.dto.Debt;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implements methods by choosing the persons with the highest and the lowest balance.
 *
 * @author dthoma
 */
public class TopBottomChooser implements PersonChooser {

    private final SortedSet<Debt> debts = new TreeSet<>();

    public TopBottomChooser(List<Debt> debtList) {
        debtList.stream().filter(d -> d.getBalance() != 0)
            .forEach(this.debts::add);
    }

    @Override
    public Debt getNextDebitor() {
        return debts.last();
    }

    @Override
    public Debt getNextCreditor() {
        return debts.first();
    }

    @Override
    public boolean personsLeft() {
        return !debts.isEmpty();
    }

    @Override
    public void updateDebt(int newBalance, Debt debt) {
        this.debts.remove(debt);
        if (newBalance != 0) {
            debt.setBalance(newBalance);
            debts.add(debt);
        }
    }

}
