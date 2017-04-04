package io.teiler.api.endpoint;

import io.teiler.api.service.GroupService;
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

    private static final String TEST_GROUP_NAME = "Test";

    @Autowired
    private GroupService groupService;

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
        String testString = TEST_GROUP_NAME;
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
        Group firstGroup = groupService.createGroup(TEST_GROUP_NAME);
        Group secondGroup = groupService.createGroup(TEST_GROUP_NAME);
        Assert.assertNotEquals(firstGroup.getId(), secondGroup.getId());
    }

    @Test
    public void testDefaultCurrency() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Assert.assertEquals(Currency.CHF, testGroup.getCurrency());
    }

    @Test
    public void testIdMatchesRequest() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Group getGroup = groupService.viewGroup(groupId);
        Assert.assertEquals(groupId, getGroup.getId());
    }

    @Test
    public void testGroupNameChanges() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Group changedGroup = new Group(null, "Neu", Currency.CHF);
        groupService.editGroup(groupId, changedGroup);
        Group newGroup = groupService.viewGroup(groupId);
        Assert.assertEquals("Neu", newGroup.getName());
    }

    @Test
    public void testCurrencyChanges() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Group changedGroup = new Group(null, TEST_GROUP_NAME, Currency.EUR);
        groupService.editGroup(groupId, changedGroup);
        Group newGroup = groupService.viewGroup(groupId);
        Assert.assertEquals(Currency.EUR, newGroup.getCurrency());
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGroupsGetDeleted() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        groupService.deleteGroup(groupId);
        groupService.viewGroup(groupId);
    }
}
