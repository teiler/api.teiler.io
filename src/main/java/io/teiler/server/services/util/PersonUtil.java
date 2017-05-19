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

import io.teiler.server.dto.Debt;
import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import io.teiler.server.util.exceptions.PersonHasUnsettledDebtsException;
import io.teiler.server.util.exceptions.PersonInactiveException;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonUtil {

    @Autowired
    private PersonRepository personRepository;

    public PersonUtil() { /* intentionally empty */ }

    /**
     * Checks whether the name of a Person is unique within a Group.
     *
     * @param groupId Id of the Group
     * @param name    Name of the Person
     * @throws PeopleNameConflictException Name already exists in Group
     */
    public void checkNamesAreUnique(String groupId, String name) throws PeopleNameConflictException {
        PersonEntity foundPerson = personRepository.getByName(groupId, name);

        // If the person was found AND is active, throw an exception
        // NEVER change the order around on this statement, otherwise you may get null pointers
        if (foundPerson != null && foundPerson.getActive()) {
            throw new PeopleNameConflictException();
        }
    }

    /**
     * Checks whether a Person exists within a Group.
     *
     * @param groupId  Id of the Group
     * @param personId Id of the Person
     * @throws PersonNotFoundException Person does not exists within Group
     */
    public void checkPersonBelongsToThisGroup(String groupId, int personId) throws PersonNotFoundException {
        if (personRepository.getByGroupAndPersonId(groupId, personId) == null) {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Checks whether a Person exists.
     *
     * @param personId Id of the Person
     * @throws PersonNotFoundException Person does not exist
     */
    public void checkPersonExists(int personId) throws PersonNotFoundException {
        if (personRepository.getById(personId) == null) {
            throw new PersonNotFoundException();
        }
    }

    public java.util.List<Person> filterInactivePeople(java.util.List<Person> people) {
        return people.stream().filter(Person::isActive).collect(Collectors.toList());
    }

    /**
     * Check if this person is active.
     *
     * @param personId The person to check
     * @throws PersonInactiveException The person is inactive
     */
    public void checkPersonIsActive(int personId) throws PersonInactiveException {
        if (!personRepository.getById(personId).getActive()) {
            throw new PersonInactiveException();
        }
    }

    public void checkPersonHasNoDebts(int personId, List<Debt> debts) {
        if (debts.stream().anyMatch(d -> d.getPerson().getId() == personId && d.getBalance() != 0)) {
            throw new PersonHasUnsettledDebtsException();
        }
    }
}
