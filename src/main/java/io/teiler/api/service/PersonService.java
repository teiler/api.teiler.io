package io.teiler.api.service;

import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.util.GroupUtil;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides service-methods for Groups.
 * 
 * @author lroellin
 */
@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    /* Spring Components (Services/Controller) */
    @Autowired
    private GroupUtil groupUtil;

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

        Person newPerson = new Person(null, name);
        if (personRepository.getByName(groupId, name)!= null) { throw new PeopleNameConflictException(); }

        PersonEntity personEntity = personRepository.create(groupId, newPerson);

        Person responsePerson = personEntity.toPerson();
        return responsePerson;
    }

    public List<Person> getPeople(String groupId, long limit) {
        groupUtil.checkIdExists(groupId);
        List<Person> people = new LinkedList<>();
        for(PersonEntity personEntity : personRepository.getPeople(groupId, limit)) {
            people.add(personEntity.toPerson());
        }
        return people;
    }
}
