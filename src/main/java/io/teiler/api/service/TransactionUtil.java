package io.teiler.api.service;

import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.util.exceptions.PayerNotFoundException;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionUtil {

    @Autowired
    private PersonUtil personUtil;

    @Autowired
    private ProfiteerRepository profiteerRepository;

    public TransactionUtil() { /* intentionally empty */ }

    /**
     * Checks whether the payer belongs to this group
     *
     * @param groupId Id of the group this payer should belong to
     * @param payerId Id of the payer
     * @throws PayerNotFoundException Payer is not found in this group
     */
    public void checkPayerBelongsToThisGroup(String groupId, Integer payerId)
        throws PayerNotFoundException {
        try {
            personUtil.checkPersonBelongsToThisGroup(groupId, payerId);
        } catch (PersonNotFoundException e) {
            throw new PayerNotFoundException(); // Throw more focused message
        }
    }

    /**
     * Checks whether the profiteer belongs to this group
     *
     * @param groupId Id of the group this profiteer should belong to
     * @param profiteerId Id of the profiteer
     * @throws ProfiteerNotFoundException Profiteer is not found in this group
     */
    public void checkProfiteerBelongsToThisGroup(String groupId, Integer profiteerId)
        throws ProfiteerNotFoundException {
        try {
            personUtil.checkPersonBelongsToThisGroup(groupId, profiteerId);
        } catch (PersonNotFoundException e) {
            throw new ProfiteerNotFoundException();
        }
    }

    /**
     * Checks whether a Profiteer-Person exists within a transaction.
     *
     * @param transactionId Id of the Compensation
     * @param profiteerPersonId Id of the Profiteer-Person
     * @throws ProfiteerNotFoundException Profiteer does not exist
     */
    public void checkProfiteerExistsInThisTransaction(int transactionId, int profiteerPersonId)
        throws ProfiteerNotFoundException {
        if (profiteerRepository
            .getByTransactionIdAndProfiteerPersonId(transactionId, profiteerPersonId) == null) {
            throw new ProfiteerNotFoundException();
        }
    }
}
