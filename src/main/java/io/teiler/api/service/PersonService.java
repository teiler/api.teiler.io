package io.teiler.api.service;

import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.util.GroupUtil;
import io.teiler.server.util.PersonUtil;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides service-methods for Groups.
 * 
 * @author lroellin
 */
@Service
public class PersonService {
    /* Spring Components (Services/Controller) */
    @Autowired
    private GroupUtil groupUtil;
    @Autowired
    private PersonUtil personUtil;

    @Autowired
    private PersonRepository personRepository;
    /**
     * Creates a new Person.
     *
     * @param groupId Group ID this person belongs to
     * @param name Name of the new Person
     * @return Information about the Person
     */
    public Person createPerson(String groupId, String name) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkNamesAreUnique(groupId, name);

        Person newPerson = new Person(null, name);
        PersonEntity personEntity = personRepository.create(groupId, newPerson);

        return personEntity.toPerson();
    }

    public List<Person> getPeople(String groupId, long limit) {
        groupUtil.checkIdExists(groupId);

        List<Person> people = new LinkedList<>();
        for(PersonEntity personEntity : personRepository.getPeople(groupId, limit)) {
            people.add(personEntity.toPerson());
        }
        return people;
    }

    public Person editPerson(String groupId, int personId, Person changedPerson) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkPersonBelongsToThisGroup(groupId, personId);
        personUtil.checkNamesAreUnique(groupId, changedPerson.getName());

        return personRepository.editPerson(personId, changedPerson).toPerson();
    }
}
