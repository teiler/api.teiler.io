package io.teiler.api.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.AuthorizationChecker;
import io.teiler.server.util.exceptions.NotAuthorizedException;

/**
 * Provides service-methods for Groups.
 * 
 * @author lroellin
 */
@Service
public class GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    /* Instance objects, potentially expensive ones you only need once */
    private SecureRandom random = new SecureRandom();
    private Gson gson = new Gson();

    /* Spring Components (Services/Controller) */
    @Autowired
    private AuthorizationChecker authorizationChecker;

    @Autowired
    private GroupRepository groupRepository;

    /* Constants */
    private static final int NUMBER_OF_ID_CHARACTERS = 8;

    // Mathematical fact, don't change it
    private static final int ENTROPY_BITS_IN_ONE_CHARACTER = 5;

    /**
     * Returns information about a Group.
     * 
     * @param authorizationHeader Id of Group
     * @return Information about the Group
     * @throws NotAuthorizedException See {@link AuthorizationChecker#checkAuthorization(String)}
     */
    public String viewGroup(String authorizationHeader) throws NotAuthorizedException {
        authorizationChecker.checkAuthorization(authorizationHeader);
        LOGGER.debug("Request with group ID: " + authorizationHeader);

        Group responseGroup = new Group(groupRepository.get(authorizationHeader));

        return gson.toJson(responseGroup);
    }

    /**
     * Creates a new Group.
     * 
     * @param name Name of the new Group
     * @return Information about the Group
     */
    public String createGroup(String name) {
        Group newGroup = new Group(null, name);

        newGroup.setUuid(createNewUuid());
        LOGGER.debug("New Group: " + newGroup.getName() + ", " + newGroup.getUuid());

        Group responseGroup =
                new Group(groupRepository.create(newGroup.getUuid(), newGroup.getName()));

        return gson.toJson(responseGroup);
    }

    /**
     * Creates a new UUID for a Group.<br>
     * See the linked page for further information.
     * 
     * @return A UUID (as a mere String; not to be confused with {@link java.util.UUID})
     * @see <a href="http://stackoverflow.com/a/41156">http://stackoverflow.com/a/41156<a>
     */
    private String createNewUuid() {
        String uuid;
        List<String> allIds = groupRepository.getAllIds();
        do {
            uuid = new BigInteger(NUMBER_OF_ID_CHARACTERS * ENTROPY_BITS_IN_ONE_CHARACTER, random)
                    .toString(32);
        } while (allIds.contains(uuid));
        return uuid;
    }

}
