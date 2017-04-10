package io.teiler.api.service;

import io.teiler.server.dto.Currency;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import io.teiler.server.util.groupid.IdGenerator;
import io.teiler.server.util.groupid.RandomGeneratorWithAlphabet;
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
    /* Constants */
    private static final int NUMBER_OF_ID_CHARACTERS = 8;
    /* Spring Components (Services/Controller) */
    @Autowired
    private GroupUtil groupUtil;
    @Autowired
    private GroupRepository groupRepository;
    private IdGenerator idGenerator = new RandomGeneratorWithAlphabet();

    /**
     * Returns information about a Group.
     * 
     * @param id Id of Group
     * @return Information about the Group
     * @throws NotAuthorizedException See {@link GroupUtil#fetchGroup(String)}
     */
    public Group viewGroup(String id) {
        groupUtil.checkIdExists(id);
        return groupUtil.fetchGroup(id);
    }

    /**
     * Creates a new Group.
     * 
     * @param name Name of the new Group
     * @return Information about the Group
     */
    public Group createGroup(String name) {
        // Default currency CHF
        Group newGroup = new Group(null, name, Currency.CHF);

        newGroup.setId(createNewId());
        LOGGER.debug("New Group: " + newGroup.getName() + ", " + newGroup.getId());

        GroupEntity groupEntity = groupRepository.create(newGroup);
        return groupEntity.toGroup();
    }

    public Group editGroup(String groupId, Group changedGroup) {
        groupUtil.checkIdExists(groupId);
        return groupRepository.editGroup(groupId, changedGroup).toGroup();
    }

    public void deleteGroup(String groupId) {
        groupUtil.checkIdExists(groupId);
        groupRepository.deleteGroup(groupId);
    }

    /**
     * Creates a new UUID for a Group.<br>
     * See the linked page for further information.
     * 
     * @return A UUID (as a mere String; not to be confused with {@link java.util.UUID})
     * @see <a href="http://stackoverflow.com/a/41156">http://stackoverflow.com/a/41156<a>
     */
    private String createNewId() {
        String id;
        do {
            id = idGenerator.generateId(NUMBER_OF_ID_CHARACTERS);
        } while (groupRepository.getGroupById(id) != null);
        return id;
    }
    
}
