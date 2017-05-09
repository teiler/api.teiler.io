package io.teiler.server.endpoints;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import io.teiler.server.dto.Person;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.PersonService;
import io.teiler.server.util.HomebrewGson;
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

    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String PERSON_ID_PARAM = ":personid";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/"
        + EndpointUtil.GROUP_ID_PARAM + "/people";
    private static final String URL_WITH_PERSON_ID = BASE_URL + "/" + PERSON_ID_PARAM;
    
    @Autowired
    private PersonService personService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Person requestPerson = HomebrewGson.getInstance().fromJson(req.body(), Person.class);
            Person newPerson = personService.createPerson(groupId, requestPerson.getName());
            return HomebrewGson.getInstance().toJson(newPerson);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            long limit = EndpointUtil.readLimit(req, DEFAULT_QUERY_LIMIT);
            Boolean activeOnly = EndpointUtil.readActive(req, true);
            List<Person> people = personService.getPeople(groupId, limit, activeOnly);
            return HomebrewGson.getInstance().toJson(people);
        });

        put(URL_WITH_PERSON_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int personId = Integer.parseInt(req.params(PERSON_ID_PARAM));
            Person changedPerson = HomebrewGson.getInstance().fromJson(req.body(), Person.class);
            // GSON doesn't go through the normal constructor so we set it manually
            changedPerson.setActive(true);
            Person person = personService.editPerson(groupId, personId, changedPerson);
            return HomebrewGson.getInstance().toJson(person);
        });

        delete(URL_WITH_PERSON_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int personId = Integer.parseInt(req.params(PERSON_ID_PARAM));
            personService.deactivatePerson(groupId, personId);
            return "";
        });

        exception(PersonNotFoundException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 404, e));

        exception(PeopleNameConflictException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 409, e));

        exception(PersonInactiveException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 410, e));
    }

}
