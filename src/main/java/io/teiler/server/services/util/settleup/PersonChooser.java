package io.teiler.server.services.util.settleup;

import io.teiler.server.dto.Debt;
import io.teiler.server.dto.Person;

import java.util.List;

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
