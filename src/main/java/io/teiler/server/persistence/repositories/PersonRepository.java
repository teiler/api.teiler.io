package io.teiler.server.persistence.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.entities.QGroupEntity;
import io.teiler.server.util.GroupFetcher;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Provides database-related operations for Groups.
 * 
 * @author lroellin
 */
@Repository
public class PersonRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GroupFetcher groupFetcher;

    /**
     * Creates a new Person and returns it.
     * 
     * @param id Id of the Group
     * @param name Name of the Group
     * @return {@link GroupEntity}
     */
    @Transactional
    public PersonEntity create(String groupId, Person person) {
        GroupEntity groupEntity = groupFetcher.fetchGroupEntity(groupId);
        PersonEntity personEntity = new PersonEntity(person);
        groupEntity.addPerson(personEntity);
        entityManager.persist(groupEntity);
        entityManager.persist(personEntity);
        return personEntity;
    }

}
