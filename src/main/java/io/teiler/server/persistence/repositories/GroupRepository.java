package io.teiler.server.persistence.repositories;

import static io.teiler.server.persistence.entities.QGroupEntity.groupEntity;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
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
public class GroupRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Creates a new Group and returns it.
     * 
     * @param id Id of the Group
     * @param name Name of the Group
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
    public GroupEntity get(String id) {
        return new JPAQuery<GroupEntity>(entityManager).from(groupEntity)
                .where(groupEntity.id.eq(id)).fetchOne();
    }

    /**
     * Returns a {@link List} of all Group-Ids.
     * 
     * @return {@link List} of Group-Ids
     */
    public List<String> getAllIds() {
        return new JPAQuery<GroupEntity>(entityManager).from(groupEntity)
                .select(groupEntity.id).fetch();
    }

}
