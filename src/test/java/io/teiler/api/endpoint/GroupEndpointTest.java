package io.teiler.api.endpoint;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;

import io.teiler.api.endpoint.util.Util;
import io.teiler.server.dto.Group;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(properties = {"local.server.port=4567"})
//@AutoConfigureTestDatabase
public class GroupEndpointTest {
    
    @LocalServerPort
    private int port;
    
    @Value("${server.ip}")
    private String ip;
    
    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Autowired
    private Util util;
    
//    @Test
    public void shouldReturn401WhenViewingGroupWithoutValidUUID() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("http://" + ip + ":" + port + "/v1/group", String.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    
//    @Test
    public void shouldReturn401WhenViewingGroupWithEmptyUUID() {
        String url = "http://" + ip + ":" + util.getPort() + "/v1/group";
        HttpEntity entity = Util.getHttpEntityWithHeader("");
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
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
