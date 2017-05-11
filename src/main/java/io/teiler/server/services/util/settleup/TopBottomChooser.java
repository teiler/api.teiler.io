package io.teiler.server.services.util.settleup;

import io.teiler.server.dto.Debt;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implements methods by choosing the persons with the highest and the lowest balance.
 *
 * @author dthoma
 */
public class TopBottomChooser implements PersonChooser {

    private final SortedMap<Integer, Debt> debts = new TreeMap<>();

    public TopBottomChooser(List<Debt> debts) {
        debts.stream().filter(d -> d.getBalance() != 0)
                .forEach(d -> this.debts.put(d.getBalance(), d));
    }

    @Override
    public Debt getNextDebitor() {
        return debts.get(debts.firstKey());
    }

    @Override
    public Debt getNextCreditor() {
        return debts.get(debts.lastKey());
    }

    @Override
    public boolean personsLeft() {
        return !debts.isEmpty();
    }

    public void updateDebt(int newBalance, Debt debt) {
        this.debts.remove(debt.getBalance(), debt);
        debt.setBalance(newBalance);
        if (newBalance != 0) {
            this.debts.put(newBalance, debt);
        }
    }

}
