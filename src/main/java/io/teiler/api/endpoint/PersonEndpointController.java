package io.teiler.api.endpoint;

import static spark.Spark.post;

import com.google.gson.Gson;
import io.teiler.api.service.PersonService;
import io.teiler.server.dto.Person;
import io.teiler.server.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Group-related endpoints.
 * 
 * @author lroellin
 */
@Controller
public class PersonEndpointController implements EndpointController {

    private Gson gson = GsonUtil.getHomebrewGson();

    @Autowired
    private PersonService personService;

    @Override
    public void register() {
        post("/v1/groups/:groupid/people", (req, res) -> {
            String groupId = req.params(":groupid");
            Person requestPerson = gson.fromJson(req.body(), Person.class);
            Person newPerson = personService.createPerson(groupId, requestPerson.getName());
            return gson.toJson(newPerson, Person.class);
        });
    }

}
