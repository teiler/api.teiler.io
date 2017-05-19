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
            // TODO: Find a better solution than converting it here
            Debt debt = new Debt(personRepository.getById(debtEntity.getPersonId()).toPerson(),
                debtEntity.getBalance());
            debts.add(debt);
        }
        LOGGER.debug("View debts: {}", debts);
        return debts;
    }

}
