package io.teiler.server.services;

import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.services.util.GroupUtil;
import io.teiler.server.services.util.PersonUtil;
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
 * @author pbaechli
 */
@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
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
        LOGGER.debug("Create person: {}", newPerson);
        PersonEntity personEntity = personRepository.create(groupId, newPerson);

        return personEntity.toPerson();
    }

    public List<Person> getPeople(String groupId, long limit, boolean activeOnly) {
        groupUtil.checkIdExists(groupId);

        List<Person> people = new LinkedList<>();
        for (PersonEntity personEntity : personRepository.getPeople(groupId, limit)) {
            people.add(personEntity.toPerson());
        }
        if (activeOnly) {
            people = personUtil.filterInactivePeople(people);
        }
        LOGGER.debug("View people: {}", people);
        return people;
    }

    public Person getPerson(String groupId, int personId) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkPersonBelongsToThisGroup(groupId, personId);
        Person person = personRepository.getById(personId).toPerson();
        LOGGER.debug("View person: {}", person);
        return person;
    }

    public Person editPerson(String groupId, int personId, Person changedPerson) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkPersonExists(personId);
        personUtil.checkPersonBelongsToThisGroup(groupId, personId);
        personUtil.checkNamesAreUnique(groupId, changedPerson.getName());
        personUtil.checkPersonIsActive(personId);
        
        /* TODO
         *   - If personId = changedPerson.id -> return person with personId
         *   - If active-flag changed -> throw exception (active flag has to
         *      be set through the corresponding service) 
         */

        LOGGER.debug("Edit person: {}", changedPerson);
        return personRepository.editPerson(personId, changedPerson).toPerson();
    }

    public void deactivatePerson(String groupId, int personId) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkPersonExists(personId);
        personUtil.checkPersonBelongsToThisGroup(groupId, personId);

        PersonEntity person = personRepository.getById(personId);
        person.setActive(false);
        LOGGER.debug("Deactivate Person: {}", personId);
        personRepository.editPerson(personId, person.toPerson());
    }

}
