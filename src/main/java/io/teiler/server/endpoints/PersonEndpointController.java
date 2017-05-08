package io.teiler.server.endpoints;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;

import io.teiler.server.dto.Person;
import io.teiler.server.services.PersonService;
import io.teiler.server.util.Error;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.Normalizer;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import io.teiler.server.util.exceptions.PersonInactiveException;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Person-related endpoints.
 * 
 * @author lroellin
 * @author pbaechli
 */
@Controller
public class PersonEndpointController implements EndpointController {

    public static final String ACTIVE_PARAM = "active";
    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String PERSON_ID_PARAM = ":personid";
    private static final String LIMIT_PARAM = "limit";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/:groupid/people";
    private static final String URL_WITH_PERSON_ID = BASE_URL + "/:personid";
    private Gson gson = GsonUtil.getHomebrewGson();
    @Autowired
    private PersonService personService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            Person requestPerson = gson.fromJson(req.body(), Person.class);
            Person newPerson = personService.createPerson(groupId, requestPerson.getName());
            return gson.toJson(newPerson);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            String limitString = req.queryParams(LIMIT_PARAM);
            long limit = DEFAULT_QUERY_LIMIT;
            if (limitString != null) {
                limit = Long.parseLong(limitString);
            }
            String activeString = req.queryParams(ACTIVE_PARAM);
            Boolean activeOnly = true;
            if(activeString != null) {
                activeOnly = Boolean.parseBoolean(activeString);
            }
            List<Person> people = personService.getPeople(groupId, limit, activeOnly);
            return gson.toJson(people);
        });

        put(URL_WITH_PERSON_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            int personId = Integer.parseInt(req.params(PERSON_ID_PARAM));
            Person changedPerson = gson.fromJson(req.body(), Person.class);
            // GSON doesn't go through the normal constructor so we set it manually
            changedPerson.setActive(true);
            Person person = personService.editPerson(groupId, personId, changedPerson);
            return gson.toJson(person);
        });

        delete(URL_WITH_PERSON_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            int personId = Integer.parseInt(req.params(PERSON_ID_PARAM));
            personService.deactivatePerson(groupId, personId);
            return "";
        });

        exception(PersonNotFoundException.class, (e, request, response) -> {
            response.status(404);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });

        exception(PeopleNameConflictException.class, (e, request, response) -> {
            response.status(409);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });

        exception(PersonInactiveException.class, (e, request, response) -> {
            response.status(410);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });
    }

}
