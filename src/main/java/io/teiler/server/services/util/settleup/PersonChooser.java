package io.teiler.server.services.util.settleup;

import io.teiler.server.dto.Debt;

/**
 * Provides methods to choose the next person to calculate suggested payments.
 *
 * @author dthoma
 */
public interface PersonChooser {

    public Debt getNextDebitor();

    public Debt getNextCreditor();

    public boolean personsLeft();
}
