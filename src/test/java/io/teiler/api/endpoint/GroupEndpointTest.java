package io.teiler.api.endpoint;

import io.teiler.api.service.GroupService;
import io.teiler.api.service.PersonService;
import io.teiler.server.Tylr;
import io.teiler.server.dto.Currency;
import io.teiler.server.dto.Group;
import io.teiler.server.util.exceptions.NotAuthorizedException;
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
public class GroupEndpointTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    PersonService personService;

    @Test(expected = NotAuthorizedException.class)
    public void testReturnNotAuthorizedWhenViewingGroupWithoutValidId() {
        groupService.viewGroup("");

    }

    @Test(expected = NotAuthorizedException.class)
    public void testReturnNotAuthorizedWhenViewingGroupWithInvalidId() {
        groupService.viewGroup("abcdef");
    }

    @Test
    public void testReturnGroupNameWhenCreating() {
        String testString = "Group Name";
        Group responseGroup = groupService.createGroup(testString);
        Assert.assertEquals(testString, responseGroup.getName());
    }

    @Test
    public void testNewIdOnEachCreation() {
        Group firstGroup = groupService.createGroup("Hello");
        Group secondGroup = groupService.createGroup("World");
        Assert.assertNotEquals(firstGroup.getId(), secondGroup.getId());
    }

    @Test
    public void testNoConflictInGroupNames() {
        Group firstGroup = groupService.createGroup("Test");
        Group secondGroup = groupService.createGroup("Test");
        Assert.assertNotEquals(firstGroup.getId(), secondGroup.getId());
    }

    @Test
    public void testDefaultCurrency() {
        Group testGroup = groupService.createGroup("Test");
        Assert.assertEquals(Currency.CHF, testGroup.getCurrency());
    }

    public void testPeopleListIsUpdated() {
        Group testGroup = groupService.createGroup("Test");
        String groupId = testGroup.getId();
        personService.createPerson(groupId, "Hans");
        personService.createPerson(groupId, "Peter");
        Assert.assertEquals("Hans", testGroup.getPeople().get(0).getName());
    }

    public void testPeopleListWhenRequested() {
        Group testGroup = groupService.createGroup("Test");
        String groupId = testGroup.getId();
        personService.createPerson(groupId, "Hans");
        personService.createPerson(groupId, "Peter");
        Group getGroup = groupService.viewGroup(groupId);
        Assert.assertEquals("Hans", getGroup.getPeople().get(0).getName());
    }

    public void testIdMatchesRequest() {
        Group testGroup = groupService.createGroup("Test");
        String groupId = testGroup.getId();
        Group getGroup = groupService.viewGroup(groupId);
        Assert.assertEquals(groupId, getGroup.getId());
    }
}
