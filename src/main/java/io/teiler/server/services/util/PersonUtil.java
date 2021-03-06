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
