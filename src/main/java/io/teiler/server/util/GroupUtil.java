package io.teiler.server.util;

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupUtil {
    GroupUtil() { /* intentionally empty */ }

    @Autowired
    private GroupRepository groupRepository;

    public void checkIdExists(String id) {
        GroupEntity groupEntity = fetchGroupEntity(id);
        if(groupEntity == null) {
            throw new NotAuthorizedException();
        }
    }

    public GroupEntity fetchGroupEntity(String id) {
        return groupRepository.get(id);
    }

    public Group fetchGroup(String id) {
        return fetchGroupEntity(id).toGroup();
     }

}
