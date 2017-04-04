package io.teiler.server.util;

import io.teiler.server.persistence.repositories.PersonRepository;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonUtil {
    PersonUtil() { /* intentionally empty */ }

    @Autowired
    private PersonRepository personRepository;

    public void checkNamesAreUnique(String groupId, String name) {
        if (personRepository.getByName(groupId, name) != null) {
            throw new PeopleNameConflictException();
        }
    }

}
