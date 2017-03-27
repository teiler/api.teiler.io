package io.teiler.server.util;

import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationChecker {

    @Autowired
    private GroupRepository groupRepository;

    public void checkAuthorization(String uuid) throws NotAuthorizedException {
        // Java checks from left to right
        if (uuid == null || groupRepository.get(uuid) == null) {
            throw new NotAuthorizedException();
        }
    }

}
