package io.teiler.server.util;

import io.teiler.api.endpoint.GroupEndpoint;
import io.teiler.server.persistence.repositories.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationChecker {

    @Autowired
    private GroupRepository groupRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationChecker.class);

    public void checkAuthorization(String uuid) throws NotAuthorizedException {
        // Java checks from left to right
        if (uuid == null || groupRepository.get(uuid) == null) {
            throw new NotAuthorizedException();
        }
    }

}
