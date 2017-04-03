package io.teiler.api.service;

import io.teiler.server.dto.Currency;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.GroupFetcher;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /* Spring Components (Services/Controller) */
    @Autowired
    private GroupFetcher groupFetcher;

    @Autowired
    private GroupRepository groupRepository;

    /* Constants */
    private static final int NUMBER_OF_ID_CHARACTERS = 8;

    // Mathematical fact, don't change it
    private static final int ENTROPY_BITS_IN_ONE_CHARACTER = 5;


    /**
     * Returns information about a Group.
     * 
     * @param id Id of Group
     * @return Information about the Group
     * @throws NotAuthorizedException See {@link GroupFetcher#fetchGroup(String)}
     */
    public Group viewGroup(String id) {
        Group group = groupFetcher.fetchGroup(id);
        return group;
    }

    /**
     * Creates a new Group.
     * 
     * @param name Name of the new Group
     * @return Information about the Group
     */
    public Group createGroup(String name) {
        // Default currency CHF
        Group newGroup = new Group(null, name, Currency.chf);

        newGroup.setId(createNewUuid());
        LOGGER.debug("New Group: " + newGroup.getName() + ", " + newGroup.getId());

        GroupEntity groupEntity = groupRepository.create(newGroup);
        Group responseGroup = groupEntity.toGroup();

        return responseGroup;
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
        do {
            uuid = new BigInteger(NUMBER_OF_ID_CHARACTERS * ENTROPY_BITS_IN_ONE_CHARACTER, random)
                    .toString(32);
        } while (groupRepository.get(uuid) != null);
        return uuid;
    }

}
