package io.teiler.server.util;

import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides checks to ensure proper authorisation before accessing any ressource(s).
 * 
 * @author lroellin
 */
@Service
public class AuthorizationChecker {

    @Autowired
    private GroupRepository groupRepository;

    /**
     * Checks whether access to this group is to be authorised.
     * 
     * @param uuid Id of the group
     * @throws NotAuthorizedException The Group-Id is <code>null</code> or does not exist in the
     *         database.
     */
    public void checkAuthorization(String uuid) throws NotAuthorizedException {
        // Java checks from left to right
        if (uuid == null || groupRepository.getGroupById(uuid) == null) {
            throw new NotAuthorizedException();
        }
    }

}
