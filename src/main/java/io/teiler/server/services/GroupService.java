package io.teiler.server.services;

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.services.util.GroupUtil;
import io.teiler.server.services.util.PersonUtil;
import io.teiler.server.services.util.groupid.IdGenerator;
import io.teiler.server.services.util.groupid.RandomGeneratorWithAlphabet;
import io.teiler.server.util.enums.Currency;
import io.teiler.server.util.exceptions.NotAuthorizedException;
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

    private static final int NUMBER_OF_ID_CHARACTERS = 8;
    private final IdGenerator idGenerator;
    
    @Autowired
    private GroupUtil groupUtil;
    
    @Autowired
    private PersonUtil personUtil;

    @Autowired
    private GroupRepository groupRepository;

    public GroupService() {
        idGenerator = new RandomGeneratorWithAlphabet();
    }

    /**
     * Returns information about a Group.
     *
     * @param id Id of Group
     * @param activeOnly Only active people
     * @return Information about the Group
     * @throws NotAuthorizedException Not authorized
     */
    public Group viewGroup(String id, Boolean activeOnly) {
        groupUtil.checkIdExists(id);
        Group group = groupRepository.getGroupById(id).toGroup();
        if (activeOnly) {
            group.setPeople(personUtil.filterInactivePeople(group.getPeople()));
        }
        LOGGER.debug("View Group: {}", group);
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
        Group newGroup = new Group(null, name, Currency.CHF);

        newGroup.setId(createNewId());
        LOGGER.debug("New Group: {}", newGroup);

        GroupEntity groupEntity = groupRepository.create(newGroup);
        return groupEntity.toGroup();
    }

    /**
     * Edits a group.
     *
     * @param groupId The group to edit
     * @param changedGroup The new group parameters
     * @return The edited group
     */
    public Group editGroup(String groupId, Group changedGroup) {
        groupUtil.checkIdExists(groupId);
        groupUtil.checkCurrencyIsValid(changedGroup);
        LOGGER.debug("Edit Group: {}", changedGroup);
        return groupRepository.editGroup(groupId, changedGroup).toGroup();
    }

    /**
     * Deletes a group.
     *
     * @param groupId The group to delete
     */
    public void deleteGroup(String groupId) {
        groupUtil.checkIdExists(groupId);
        LOGGER.debug("Delete Group: {}", groupId);
        groupRepository.deleteGroup(groupId);
    }

    /**
     * Creates a new UUID for a Group.<br>
     * See the linked page for further information.
     *
     * @return A UUID (as a mere String; not to be confused with {@link java.util.UUID})
     * @see <a href="http://stackoverflow.com/a/41156">http://stackoverflow.com/a/41156</a>
     */
    private String createNewId() {
        String id;
        do {
            id = idGenerator.generateId(NUMBER_OF_ID_CHARACTERS);
        } while (groupRepository.getGroupById(id) != null);
        return id;
    }

}
