package io.teiler.api.endpoint;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import io.teiler.api.service.PersonService;
import io.teiler.server.dto.Person;
import io.teiler.server.util.Error;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Group-related endpoints.
 * @author lroellin
 */
@Controller
public class PersonEndpointController implements EndpointController {

    private Gson gson = GsonUtil.getHomebrewGson();

    @Autowired
    private PersonService personService;

    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String PERSON_ID_PARAM = ":personid";
    private static final String LIMIT_PARAM = "limit";

    @Override
    public void register() {
        post("/v1/groups/:groupid/people", (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            Person requestPerson = gson.fromJson(req.body(), Person.class);
            Person newPerson = personService.createPerson(groupId, requestPerson.getName());
            return gson.toJson(newPerson);
        });

        get("/v1/groups/:groupid/people", (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            String limitString = req.queryParams(LIMIT_PARAM);
            long limit = DEFAULT_QUERY_LIMIT;
            if (limitString != null) {
                limit = Long.parseLong(limitString);
            }
            List<Person> people = personService.getPeople(groupId, limit);
            return gson.toJson(people);
        });

        put("/v1/groups/:groupid/people/:personid", (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            int personId = Integer.parseInt(req.params(PERSON_ID_PARAM));
            Person changedPerson = gson.fromJson(req.body(), Person.class);
            Person person = personService.editPerson(groupId, personId, changedPerson);
            return gson.toJson(person);
        });

        delete("/v1/groups/:groupid/people/:personid", (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            int personId = Integer.parseInt(req.params(":personid"));
            personService.deletePerson(groupId, personId);
            return "";
        });

        exception(PersonNotFoundException.class, (e, request, response) -> {
            response.status(404);
            Error error = new Error("PERSON_NOT_FOUND");
            response.body(gson.toJson(error));
        });

        exception(PeopleNameConflictException.class, (e, request, response) -> {
            response.status(409);
            Error error = new Error("PEOPLE_NAME_CONFLICT");
            response.body(gson.toJson(error));
        });
    }

}
