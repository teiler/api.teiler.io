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

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.CurrencyNotValidException;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupUtil {

    @Autowired
    private GroupRepository groupRepository;

    public GroupUtil() { /* intentionally empty */ }

    /**
     * Checks whether a Group-Id exists.
     *
     * @param id Id of the group
     * @throws NotAuthorizedException Group-Id does not exist
     */
    public void checkIdExists(String id) throws NotAuthorizedException {
        GroupEntity groupEntity = groupRepository.getGroupById(id);
        if (groupEntity == null) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * Checks if the currency in the given group is a valid one.
     *
     * <p>
     * This is done by looking if it's null.
     * If it is null, then GSON could'nt correctly map the textual currency to the enum.
     * Therefore, the given currency is invalid
     * </p>
     *
     * @param changedGroup The group object to check
     * @throws CurrencyNotValidException the given currency is invalid
     */
    public void checkCurrencyIsValid(Group changedGroup) throws CurrencyNotValidException {
        if (changedGroup.getCurrency() == null) {
            throw new CurrencyNotValidException();
        }
    }

}
