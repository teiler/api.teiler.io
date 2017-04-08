package io.teiler.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.NotAuthorizedException;

@Service
public class GroupUtil {
    
    GroupUtil() { /* intentionally empty */ }

    @Autowired
    private GroupRepository groupRepository;

    public void checkIdExists(String id) {
        GroupEntity groupEntity = fetchGroupEntity(id);
        if (groupEntity == null) {
            throw new NotAuthorizedException();
        }
    }

    public GroupEntity fetchGroupEntity(String id) {
        return groupRepository.getGroupById(id);
    }

    public Group fetchGroup(String id) {
        return fetchGroupEntity(id).toGroup();
    }

}
