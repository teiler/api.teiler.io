package io.teiler.server.services.util.settleup;

import io.teiler.server.dto.Debt;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implements methods by choosing the persons with the highest and the lowest balance.
 *
 * @author dthoma
 */
public class TopBottomChooser implements PersonChooser {

    private final SortedMap<Integer, Debt> debts;
    
    public TopBottomChooser() {
        debts = new TreeMap<>();
    }

    public TopBottomChooser(SortedMap<Integer, Debt> debts) {
        this.debts = debts;
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
        return debts.entrySet().stream().anyMatch(d -> d.getKey() != 0);
    }

}
