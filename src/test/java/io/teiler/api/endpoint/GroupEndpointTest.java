package io.teiler.api.endpoint;

import com.google.gson.Gson;
import io.teiler.api.endpoint.util.Util;
import io.teiler.api.service.GroupService;
import io.teiler.server.Tylr;
import io.teiler.server.dto.Group;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@AutoConfigureTestDatabase
public class GroupEndpointTest {
    
    @LocalServerPort
    private int port;
    
    @Value("${server.ip}")
    private String ip;
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private Util util;
    
    @Test(expected = NotAuthorizedException.class)
    public void shouldReturnNotAuthorizedWhenViewingGroupWithoutValidUUID()
        throws NotAuthorizedException {
        groupService.viewGroup("");
    }

//    @Test
    public void shouldReturn401WhenViewingGroupWithInvalidUUID() {
        String url = "http://" + ip + ":" + util.getPort() + "/v1/group";
        HttpEntity entity = Util.getHttpEntityWithHeader("abcdef");
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

//    @Test
    public void shouldReturnGroupNameWhenCreating() {
        String url = "http://" + ip + ":" + util.getPort() + "/v1/group";
        String testString = "Group Name";
        Group testGroup = new Group("", testString);
        Gson gson = new Gson();

        ResponseEntity<String> response = testRestTemplate.postForEntity(url, gson.toJson(testGroup), String.class);
        Group responseGroup = gson.fromJson(response.getBody(), Group.class);
        Assert.assertEquals(testString, responseGroup.getName());
    }

}
