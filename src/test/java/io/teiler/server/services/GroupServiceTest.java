package io.teiler.server.services;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Group;
import io.teiler.server.util.enums.Currency;
import io.teiler.server.util.exceptions.CurrencyNotValidException;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@ActiveProfiles("test")
public class GroupServiceTest {

    private static final String TEST_GROUP_NAME = "Test";

    @Autowired
    private GroupService groupService;

    @Test(expected = NotAuthorizedException.class)
    public void testReturnNotAuthorizedWhenViewingGroupWithoutValidId() {
        groupService.viewGroup("", true);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testReturnNotAuthorizedWhenViewingGroupWithInvalidId() {
        groupService.viewGroup("abcdef", true);
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
        Group getGroup = groupService.viewGroup(groupId, true);
        Assert.assertEquals(groupId, getGroup.getId());
    }

    @Test
    public void testGroupNameChanges() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Group changedGroup = new Group(null, "Neu", Currency.CHF);
        groupService.editGroup(groupId, changedGroup);
        Group newGroup = groupService.viewGroup(groupId, true);
        Assert.assertEquals("Neu", newGroup.getName());
    }

    @Test(expected = CurrencyNotValidException.class)
    public void testInvalidCurrencyIsDetected() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        // We have to use null since we can't really fake an invalid enum value
        Group changedGroup = new Group(null, TEST_GROUP_NAME, null);
        groupService.editGroup(groupId, changedGroup);
    }

    @Test
    public void testCurrencyChanges() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Group changedGroup = new Group(null, TEST_GROUP_NAME, Currency.EUR);
        groupService.editGroup(groupId, changedGroup);
        Group newGroup = groupService.viewGroup(groupId, true);
        Assert.assertEquals(Currency.EUR, newGroup.getCurrency());
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGroupsGetDeleted() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        groupService.deleteGroup(groupId);
        groupService.viewGroup(groupId, true);
    }

}
