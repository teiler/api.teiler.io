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
