package io.teiler.api.service;

import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.util.GroupFetcher;
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
    private GroupFetcher groupFetcher;

    @Autowired
    private PersonRepository personRepository;
    /**
     * Creates a new Person.
     *
     * @param group Group ID this person belongs to
     * @param name Name of the new Person
     * @return Information about the Person
     */
    public Person createPerson(String groupId, String name) {


        Person newPerson = new Person(null, name);

        PersonEntity personEntity = personRepository.create(groupId, newPerson);

        Person responsePerson = personEntity.toPerson();
        return responsePerson;
    }

}
