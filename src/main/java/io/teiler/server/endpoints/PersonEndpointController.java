package io.teiler.server.endpoints;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import io.teiler.server.dto.Person;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.PersonService;
import io.teiler.server.util.Error;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.exceptions.PeopleNameConflictException;
import io.teiler.server.util.exceptions.PersonInactiveException;
import io.teiler.server.util.exceptions.PersonNotFoundException;

/**
 * Controller for Person-related endpoints.
 *
 * @author lroellin
 * @author pbaechli
 */
@Controller
public class PersonEndpointController implements EndpointController {

    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String PERSON_ID_PARAM = ":personid";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/" + EndpointUtil.GROUP_ID_PARAM + "/people";
    private static final String URL_WITH_PERSON_ID = BASE_URL + "/" + PERSON_ID_PARAM;
    
    private Gson gson = GsonUtil.getHomebrewGson();
    
    @Autowired
    private PersonService personService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Person requestPerson = gson.fromJson(req.body(), Person.class);
            Person newPerson = personService.createPerson(groupId, requestPerson.getName());
            return gson.toJson(newPerson);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            long limit = EndpointUtil.readLimit(req, DEFAULT_QUERY_LIMIT);
            Boolean activeOnly = EndpointUtil.readActive(req, true);
            List<Person> people = personService.getPeople(groupId, limit, activeOnly);
            return gson.toJson(people);
        });

        put(URL_WITH_PERSON_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int personId = Integer.parseInt(req.params(PERSON_ID_PARAM));
            Person changedPerson = gson.fromJson(req.body(), Person.class);
            // GSON doesn't go through the normal constructor so we set it manually
            changedPerson.setActive(true);
            Person person = personService.editPerson(groupId, personId, changedPerson);
            return gson.toJson(person);
        });

        delete(URL_WITH_PERSON_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
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
