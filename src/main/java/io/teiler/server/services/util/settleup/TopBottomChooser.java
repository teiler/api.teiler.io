package io.teiler.server.services.util.settleup;

import io.teiler.server.dto.Debt;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods by choosing the persons with the highest and the lowest balance.
 *
 * @author dthoma
 */
public class TopBottomChooser implements PersonChooser {

    private final ArrayList<Debt> debts = new ArrayList<>();

    public TopBottomChooser(List<Debt> debts) {
        debts.stream().filter(d -> d.getBalance() != 0)
            .forEach(this.debts::add);
        debts.sort(Debt::compareTo);
    }

    @Override
    public Debt getNextDebitor() {
        return debts.get(debts.size() - 1);
    }

    @Override
    public Debt getNextCreditor() {
        return debts.get(0);
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
        debts.sort(Debt::compareTo);
    }

}
