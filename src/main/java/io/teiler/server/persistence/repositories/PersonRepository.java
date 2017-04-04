package io.teiler.server.persistence.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.entities.QPersonEntity;
import io.teiler.server.util.GroupUtil;
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
    private GroupUtil groupUtil;

    /**
     * Creates a new Person and returns it.
     * 
     * @param id Id of the Group
     * @param name Name of the Group
     * @return {@link GroupEntity}
     */
    @Transactional
    public PersonEntity create(String groupId, Person person) {
        GroupEntity groupEntity = groupUtil.fetchGroupEntity(groupId);
        PersonEntity personEntity = new PersonEntity(person);
        groupEntity.addPerson(personEntity);
        entityManager.persist(personEntity);
        entityManager.persist(groupEntity);
        return personEntity;
    }

    public PersonEntity getByName(String groupId, String name) {
        return new JPAQuery<PersonEntity>(entityManager).from(QPersonEntity.personEntity)
            .where(QPersonEntity.personEntity.groupId.eq(groupId)
            .and(QPersonEntity.personEntity.name.eq(name))).
            fetchOne();
    }

    public List<PersonEntity> getPeople(String groupId, long limit) {
        return new JPAQuery<PersonEntity>(entityManager).from(QPersonEntity.personEntity)
            .where(QPersonEntity.personEntity.groupId.eq(groupId))
            .limit(limit)
            .fetch();
    }
}
