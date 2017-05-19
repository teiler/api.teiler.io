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
package io.teiler.server.services.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.util.exceptions.PayerInactiveException;
import io.teiler.server.util.exceptions.PayerNotFoundException;
import io.teiler.server.util.exceptions.PersonInactiveException;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import io.teiler.server.util.exceptions.ProfiteerInactiveException;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;

@Service
public class TransactionUtil {

    @Autowired
    private PersonUtil personUtil;

    @Autowired
    private ProfiteerRepository profiteerRepository;

    public TransactionUtil() { /* intentionally empty */ }

    /**
     * Checks whether the payer belongs to this group.
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
     * Checks whether the profiteer belongs to this group.
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

    /**
     * Checks if the payer is active.
     *
     * @param payerId Id of the payer
     * @throws PayerInactiveException The payer is not active
     */
    public void checkPayerIsActive(int payerId) throws PayerInactiveException {
        try {
            personUtil.checkPersonIsActive(payerId);
        } catch (PersonInactiveException e) {
            throw new PayerInactiveException();
        }
    }

    /**
     * Checks if the profiteer is active
     *
     * @param profiteerId Id of the profiteer
     * @throws io.teiler.server.util.exceptions.ProfiteerInactiveException The payer is not active
     */
    public void checkProfiteerIsActive(int profiteerId) throws ProfiteerInactiveException {
        try {
            personUtil.checkPersonIsActive(profiteerId);
        } catch (PersonInactiveException e) {
            throw new ProfiteerInactiveException();
        }
    }

}
