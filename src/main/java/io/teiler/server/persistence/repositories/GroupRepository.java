package io.teiler.server.persistence.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.entities.QGroupEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by lroellin on 27.03.17.
 */
@Repository
public class GroupRepository {
    @Autowired
    private EntityManager entityManager;

    //We could use a Group as the parameter in this case, but since
    // you usually only have the attributes and not a whole class now
    // we use the separated parameters.
    @Transactional
    public GroupEntity create(String uuid, String name) {
        GroupEntity groupEntity = new GroupEntity(uuid, name);
        entityManager.persist(groupEntity);
        return groupEntity;
    }

    public GroupEntity get(String uuid) {
        return new JPAQuery<GroupEntity>(entityManager).from(QGroupEntity.groupEntity)
            .where(QGroupEntity.groupEntity.uuid.eq(uuid)).fetchOne();
    }

    public List<String> getAllIds() {
        return new JPAQuery<GroupEntity>(entityManager).from(QGroupEntity.groupEntity)
            .select(QGroupEntity.groupEntity.uuid).fetch();
    }
}
