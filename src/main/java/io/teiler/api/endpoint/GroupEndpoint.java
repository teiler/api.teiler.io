package io.teiler.api.endpoint;

import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import io.teiler.server.dto.Group;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.AuthorizationChecker;


@Controller
public class GroupEndpoint implements Endpoint {

    private SecureRandom random = new SecureRandom();
    private Gson gson = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndpoint.class);

    @Autowired
    private AuthorizationChecker authorizationChecker;
    @Autowired
    private GroupRepository groupRepository;

    private static final String GROUP_ID_HEADER = "X-Teiler-GroupID";
    private static final int NUMBER_OF_ID_CHARACTERS = 8;
    // Mathematical fact, don't change it
    private static final int ENTROPY_BITS_IN_ONE_CHARACTER = 5;


    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call to '" + req.pathInfo() + "'"));

        post("/v1/group", (req, res) -> {
            awaitInitialization();
            
            Group requestGroup = gson.fromJson(req.body(), Group.class);

            requestGroup.setUuid(createNewUuid());
            LOGGER.debug("New Group: " + requestGroup.getName() + ", " + requestGroup.getUuid());

            Group responseGroup = new Group(
                groupRepository.create(requestGroup.getUuid(), requestGroup.getName()));

            return gson.toJson(responseGroup);
        });

        get("/v1/group", (req, res) -> {
            awaitInitialization();
            
            String uuid = req.headers(GROUP_ID_HEADER);
            authorizationChecker.checkAuthorization(uuid);
            LOGGER.debug("Request with group ID: " + uuid);

            Group responseGroup = new Group(groupRepository.get(uuid));

            return gson.toJson(responseGroup);
        });


    }

    // this needs to go somewhere else
    private String createNewUuid() {
        String uuid;
        List<String> allIds = groupRepository.getAllIds();
        // reasoning: http://stackoverflow.com/a/41156
        do {
            uuid = new BigInteger(NUMBER_OF_ID_CHARACTERS * ENTROPY_BITS_IN_ONE_CHARACTER, random).toString(32);
        } while (allIds.contains(uuid));
        return uuid;
    }
}
