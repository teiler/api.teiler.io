package io.teiler.server.services;

import io.teiler.server.dto.Debt;
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

    @Autowired
    private GroupUtil groupUtil;

    @Autowired
    private PersonUtil personUtil;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DebtService debtService;

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

    /**
     * Gets all the people in a group up to a given limit.
     *
     * @param groupId The group to get the people for
     * @param limit How many people to get
     * @param activeOnly Get only active people
     * @return The people in a group
     */
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

    /**
     * Gets a single person in a group.
     *
     * @param groupId The group to get this person from
     * @param personId The person to get
     * @return The person found
     */
    public Person getPerson(String groupId, int personId) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkPersonBelongsToThisGroup(groupId, personId);
        Person person = personRepository.getById(personId).toPerson();
        LOGGER.debug("View person: {}", person);
        return person;
    }

    /**
     * Edits a person.
     *
     * @param groupId The group to edit this person in
     * @param personId The person to edit
     * @param changedPerson The new parameters for the person
     * @return The edited person
     */
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

    /**
     * Deactivates a person in a group.
     *
     * @param groupId The group to deactivate the person in
     * @param personId The person to deactivate
     */
    public void deactivatePerson(String groupId, int personId) {
        groupUtil.checkIdExists(groupId);
        personUtil.checkPersonExists(personId);
        personUtil.checkPersonBelongsToThisGroup(groupId, personId);
        // This could be done in the util layer but then we have up-references
        List<Debt> debts = debtService.getDebts(groupId);
        personUtil.checkPersonHasNoDebts(personId, debts);

        PersonEntity person = personRepository.getById(personId);
        person.setActive(false);
        LOGGER.debug("Deactivate Person: {}", personId);
        personRepository.editPerson(personId, person.toPerson());
    }

}
