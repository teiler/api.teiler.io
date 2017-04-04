package io.teiler.api.endpoint;

import io.teiler.api.service.GroupService;
import io.teiler.api.service.PersonService;
import io.teiler.server.Tylr;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import java.util.List;
import org.junit.Assert;
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

    private static final String FIRST_PERSON_NAME = "Hans";
    private static final String SECOND_PERSON_NAME = "Peter";
    private static final String TEST_GROUP_NAME = "Test";

    @Autowired
    private PersonService personService;
    @Autowired
    private GroupService groupService;

    @Test(expected = NotAuthorizedException.class)
    public void testReturnNotAuthorizedWhenCreatingPersonWithInvalidGroupId() {
        personService.createPerson("", FIRST_PERSON_NAME);
    }

    @Test(expected = PeopleNameConflictException.class)
    public void testReturnPeopleNameConflict() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        personService.createPerson(groupId, FIRST_PERSON_NAME);
        personService.createPerson(groupId, FIRST_PERSON_NAME);
    }
    @Test
    public void testDifferentNamesDontConflict() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        personService.createPerson(groupId, FIRST_PERSON_NAME);
        personService.createPerson(groupId, SECOND_PERSON_NAME);
    }

    @Test
    public void testPeopleListWhenRequested() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        personService.createPerson(groupId, FIRST_PERSON_NAME);
        personService.createPerson(groupId, SECOND_PERSON_NAME);
        Group getGroup = groupService.viewGroup(groupId);
        Assert.assertEquals("Hans", getGroup.getPeople().get(0).getName());
    }

    @Test
    public void testPeopleListSize() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        personService.createPerson(groupId, FIRST_PERSON_NAME);
        personService.createPerson(groupId, SECOND_PERSON_NAME);
        List<Person> people = personService.getPeople(groupId, 20);
        Assert.assertEquals(2, people.size());
    }

    @Test
    public void testPeopleListContainsHans() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        personService.createPerson(groupId, FIRST_PERSON_NAME);
        personService.createPerson(groupId, SECOND_PERSON_NAME);
        List<Person> people = personService.getPeople(groupId, 20);
        Assert.assertEquals("Hans", people.get(0).getName());
    }



}
