package io.teiler.server.services;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Debt;
import io.teiler.server.persistence.entities.DebtEntity;
import io.teiler.server.persistence.repositories.DebtRepository;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.services.util.GroupUtil;

/**
 * Provides service-methods for debts.
 *
 * @author dthoma
 */
@Service
public class DebtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebtService.class);

    @Autowired
    private GroupUtil groupUtil;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DebtRepository debtRepository;

    /**
     * Gets the debts for the given group.
     *
     * @param groupId The group to get the debts for
     * @return The debts for that group
     */
    public List<Debt> getDebts(String groupId) {
        groupUtil.checkIdExists(groupId);

        List<Debt> debts = new LinkedList<>();
        for (DebtEntity debtEntity : debtRepository.get(groupId)) {
            Debt debt = new Debt(personRepository.getById(debtEntity.getPersonId()).toPerson(),
                debtEntity.getBalance());
            debts.add(debt);
        }
        LOGGER.debug("View debts: {}", debts);
        return debts;
    }

}
