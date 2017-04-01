package io.teiler.server.persistence.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.entities.QGroupEntity;

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
     * <i>We could use a Group as the parameter in this case, but since you usually only have the
     * attributes and not a whole class now we use the separated parameters. </i>
     * 
     * @param uuid Id of the Group
     * @param name Name of the Group
     * @return {@link GroupEntity}
     */
    @Transactional
    public GroupEntity create(String uuid, String name) {
        GroupEntity groupEntity = new GroupEntity(uuid, name);
        entityManager.persist(groupEntity);
        return groupEntity;
    }

    /**
     * Returns the {@link GroupEntity} with the given Id.
     * 
     * @param uuid Id of the Group
     * @return {@link GroupEntity}
     */
    public GroupEntity get(String uuid) {
        return new JPAQuery<GroupEntity>(entityManager).from(QGroupEntity.groupEntity)
                .where(QGroupEntity.groupEntity.uuid.eq(uuid)).fetchOne();
    }

    /**
     * Returns a {@link List} of all Group-Ids.
     * 
     * @return {@link List} of Group-Ids
     */
    public List<String> getAllIds() {
        return new JPAQuery<GroupEntity>(entityManager).from(QGroupEntity.groupEntity)
                .select(QGroupEntity.groupEntity.uuid).fetch();
    }

}
