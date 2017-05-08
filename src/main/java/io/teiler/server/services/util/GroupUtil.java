package io.teiler.server.services.util;

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.exceptions.CurrencyNotValidException;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupUtil {

    @Autowired
    private GroupRepository groupRepository;

    GroupUtil() { /* intentionally empty */ }

    /**
     * Checks whether a Group-Id exists.
     *
     * @param id Id of the group
     * @throws NotAuthorizedException Group-Id does not exist
     */
    public void checkIdExists(String id) throws NotAuthorizedException {
        GroupEntity groupEntity = groupRepository.getGroupById(id);
        if (groupEntity == null) {
            throw new NotAuthorizedException();
        }
    }

    public void checkCurrencyIsValid(Group changedGroup) throws CurrencyNotValidException {
        if (changedGroup.getCurrency() == null) {
            throw new CurrencyNotValidException();
        }
    }

}
