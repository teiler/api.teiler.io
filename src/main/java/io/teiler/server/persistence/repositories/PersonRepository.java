package io.teiler.server.persistence.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import io.teiler.server.dto.Person;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.entities.PersonEntity;
import io.teiler.server.persistence.entities.QPersonEntity;

/**
 * Provides database-related operations for Groups.
 *
 * @author lroellin
 * @author pbaechli
 */
@Repository
public class PersonRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    /**
     * Creates a new Person and returns it.
     *
     * @param groupId Id of the Group
     * @param person Name of the Group
     * @return {@link GroupEntity}
     */
    @Transactional
    public PersonEntity create(String groupId, Person person) {
        GroupEntity groupEntity = groupRepository.getGroupById(groupId);
        PersonEntity personEntity = new PersonEntity(person);
        groupEntity.addPerson(personEntity);
        entityManager.persist(personEntity);
        entityManager.persist(groupEntity);
        return personEntity;
    }

    /**
     * Returns a {@link PersonEntity} with the given name and Group-Id.
     *
     * @param groupId Id of the Group
     * @param name Name of the Person
     * @return {@link PersonEntity}
     */
    public PersonEntity getByName(String groupId, String name) {
        return new JPAQuery<PersonEntity>(entityManager).from(QPersonEntity.personEntity)
            .where(QPersonEntity.personEntity.groupId.eq(groupId)
                .and(QPersonEntity.personEntity.name.eq(name)))
            .fetchOne();
    }

    /**
     * Returns a {@link List} of {@link PersonEntity} in the Group with the given Id.
     *
     * @param groupId Id of the Group
     * @param limit Maximum amount of people to fetch
     * @return {@link List} of {@link PersonEntity}
     */
    public List<PersonEntity> getPeople(String groupId, long limit) {
        return new JPAQuery<PersonEntity>(entityManager).from(QPersonEntity.personEntity)
            .where(QPersonEntity.personEntity.groupId.eq(groupId))
            .limit(limit)
            .orderBy(QPersonEntity.personEntity.id.asc())
            .fetch();
    }

    /**
     * Returns a {@link PersonEntity} with the given Group- and Person-Id.
     *
     * @param groupId Id of the Group
     * @param personId Id of the Person
     * @return {@link PersonEntity}
     */
    public PersonEntity getByGroupAndPersonId(String groupId, int personId) {
        return new JPAQuery<PersonEntity>(entityManager).from(QPersonEntity.personEntity)
            .where(QPersonEntity.personEntity.groupId.eq(groupId)
                .and(QPersonEntity.personEntity.id.eq(personId)))
            .fetchOne();
    }

    /**
     * Returns the {@link PersonEntity} with the given Id.
     *
     * @param personId Id of the Person
     * @return {@link PersonEntity}
     */
    public PersonEntity getById(int personId) {
        return new JPAQuery<PersonEntity>(entityManager).from(QPersonEntity.personEntity)
            .where(QPersonEntity.personEntity.id.eq(personId))
            .fetchOne();
    }

    /**
     * Updates a already persisted Person with the given values.
     *
     * @param personId Id of the Person
     * @param changedPerson {@link Person} containing the new values
     * @return {@link PersonEntity} containing the new values
     */
    @Transactional
    public PersonEntity editPerson(int personId, Person changedPerson) {
        PersonEntity existingPerson = getById(personId);

        PersonEntity updatedPerson = new PersonEntity(changedPerson);
        updatedPerson.setId(existingPerson.getId());
        updatedPerson.setGroupId(existingPerson.getGroupId());

        return entityManager.merge(updatedPerson);
    }

}
