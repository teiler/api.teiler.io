package io.teiler.api.service;

import io.teiler.server.dto.Person;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonUtil {
    
    @Autowired
    private PersonRepository personRepository;

    public PersonUtil() { /* intentionally empty */ }

    /**
     * Checks whether the name of a Person is unique within a Group.
     * 
     * @param groupId Id of the Group
     * @param name Name of the Person
     * @throws PeopleNameConflictException Name already exists in Group 
     */
    public void checkNamesAreUnique(String groupId, String name) throws PeopleNameConflictException {
        if (personRepository.getByName(groupId, name) != null) {
            throw new PeopleNameConflictException();
        }
    }

    /**
     * Checks whether a Person exists within a Group.
     * 
     * @param groupId Id of the Group
     * @param personId Id of the Person
     * @throws PersonNotFoundException Person does not exists within Group
     */
    public void checkPersonBelongsToThisGroup(String groupId, int personId)throws PersonNotFoundException {
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
    
}
