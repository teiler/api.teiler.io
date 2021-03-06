package io.teiler.server.persistence.repositories;

import static io.teiler.server.persistence.entities.QGroupEntity.groupEntity;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
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
public class GroupRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Creates a new Group and returns it.
     *
     * @param group {@link Group}
     * @return {@link GroupEntity}
     */
    @Transactional
    public GroupEntity create(Group group) {
        GroupEntity groupEntity = new GroupEntity(group);
        entityManager.persist(groupEntity);
        return groupEntity;
    }

    /**
     * Returns the {@link GroupEntity} with the given Id.
     *
     * @param id Id of the Group
     * @return {@link GroupEntity}
     */
    public GroupEntity getGroupById(String id) {
        return new JPAQuery<GroupEntity>(entityManager).from(groupEntity)
            .where(groupEntity.id.eq(id)).fetchOne();
    }

    /**
     * Updates a already persisted Group with the given values.
     *
     * @param groupId Id of the Group
     * @param changedGroup {@link Group} containing the new values
     * @return {@link GroupEntity} containing the new values
     */
    @Transactional
    public GroupEntity editGroup(String groupId, Group changedGroup) {
        GroupEntity group = getGroupById(groupId);
        group.setName(changedGroup.getName());
        group.setCurrency(changedGroup.getCurrency());
        entityManager.persist(group);
        return group;
    }

    /**
     * Deletes the Group with the given Id.
     *
     * @param groupId Id of the Group
     */
    @Transactional
    public void deleteGroup(String groupId) {
        GroupEntity group = getGroupById(groupId);
        entityManager.remove(group);
    }

}
