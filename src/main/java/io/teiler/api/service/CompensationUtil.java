package io.teiler.api.service;

import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.persistence.repositories.CompensationRepository;
import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.util.exceptions.PayerProfiteerConflictException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationUtil {
    
    @Autowired
    private CompensationRepository compensationRepository;
    
    @Autowired
    private ProfiteerRepository profiteerRepository;
    
    public CompensationUtil() { /* intentionally empty */ }

    /**
     * Checks whether a Compensation exists within a Group.
     * 
     * @param groupId Id of the Group
     * @param compensationId Id of the Compensation
     * @throws TransactionNotFoundException Compensation does not exists within Group
     */
    public void checkCompensationBelongsToThisGroup(String groupId, int compensationId) throws TransactionNotFoundException {
        if (compensationRepository.getByGroupIdAndCompensationId(groupId, compensationId) == null) {
            throw new TransactionNotFoundException();
        }
    }

    /**
     * Checks whether a Compensation exists.
     *
     * @param compensationId Id of the Compensation
     * @throws TransactionNotFoundException Compensation does not exist
     */
    public void checkCompensationExists(int compensationId) throws TransactionNotFoundException {
        if (compensationRepository.getById(compensationId) == null) {
            throw new TransactionNotFoundException();
        }
    }

    /**
     * Checks whether the Payer and the Profiteer are equal
     *
     * @param compensation The compensation to check
     * @param profiteer The profiteer to check
     * @throws PayerProfiteerConflictException Profiteer and payer are equal in that compensation
     */
    public void checkPayerAndProfiteerAreNotEqual(Compensation compensation, Profiteer profiteer)
        throws PayerProfiteerConflictException {
        if (compensation.getPayer().getId().compareTo(profiteer.getPerson().getId()) == 0) {
            throw new PayerProfiteerConflictException();
        }
    }
    
}
