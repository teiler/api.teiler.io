package io.teiler.api.endpoint;

import io.teiler.api.service.GroupService;
import io.teiler.api.service.PersonService;
import io.teiler.server.Tylr;
import io.teiler.server.dto.Group;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@AutoConfigureTestDatabase
public class PersonEndpointTest {

    @Autowired
    private PersonService personService;
    @Autowired
    private GroupService groupService;

    @Test(expected = NotAuthorizedException.class)
    public void testReturnNotAuthorizedWhenCreatingPersonWithInvalidGroupId() {
        personService.createPerson("", "Fritz");
    }

    @Test(expected = PeopleNameConflictException.class)
    public void testReturnPeopleNameConflict() {
        Group testGroup = groupService.createGroup("Test");
        String groupId = testGroup.getId();
        personService.createPerson(groupId, "Hans");
        personService.createPerson(groupId, "Hans");
    }

    public void testDifferentNamesDontConflict() {
        Group testGroup = groupService.createGroup("Test");
        String groupId = testGroup.getId();
        personService.createPerson(groupId, "Hans");
        personService.createPerson(groupId, "Peter");
    }

    
}
