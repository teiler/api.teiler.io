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
    public void shouldReturnNotAuthorizedWhenViewingGroupWithoutValidUUID()
        throws NotAuthorizedException {
        groupService.viewGroup("");
    }

    @Test(expected = NotAuthorizedException.class)
    public void shouldReturnNotAuthorizedWhenViewingGroupWithInvalidUUID()
        throws NotAuthorizedException {
        groupService.viewGroup("abcdef");
    }

    @Test
    public void shouldReturnGroupNameWhenCreating() {
        String testString = "Group Name";
        String response = groupService.createGroup(testString);
        Group responseGroup = gson.fromJson(response, Group.class);
        Assert.assertEquals(testString, responseGroup.getName());
    }
    
}
