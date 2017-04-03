package io.teiler.api.endpoint;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import io.teiler.api.service.GroupService;
import io.teiler.server.Tylr;
import io.teiler.server.dto.Group;
import io.teiler.server.util.exceptions.NotAuthorizedException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@AutoConfigureTestDatabase
public class GroupEndpointTest {

    Gson gson = new Gson();
    
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


}
