package io.teiler.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.persistence.repositories.CompensationRepository;
import io.teiler.server.util.exceptions.TransactionNotFoundException;

@Service
public class CompensationUtil {
    
    @Autowired
    private CompensationRepository compensationRepository;
    
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
    
}
