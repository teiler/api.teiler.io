package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.repositories.GroupRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.math.BigInteger;


@Component
public class GroupEndpoint implements Endpoint {

    private SecureRandom random = new SecureRandom();
    private Gson gson = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndpoint.class);
    @Autowired
    private GroupRepository groupRepository;

    private static final int NUMBER_OF_ID_CHARACTERS = 8;
    private static final String GROUP_ID_HEADER = "X-Teiler-GroupID";

    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call to '" + req.pathInfo() + "'"));

        post("/v1/group", (req, res) -> {
            Group requestGroup = gson.fromJson(req.body(), Group.class);

            requestGroup.setUuid(createNewUuid());
            LOGGER.debug("New Group: " + requestGroup.getName() + ", " + requestGroup.getUuid());

            Group responseGroup = new Group(
                groupRepository.create(requestGroup.getUuid(), requestGroup.getName()));

            return gson.toJson(responseGroup);
        });

        get("/v1/group", (req, res) -> {
            String uuid = req.headers(GROUP_ID_HEADER);
            LOGGER.debug("Request with group ID: " + uuid);

            Group responseGroup = new Group(groupRepository.get(uuid));

            return gson.toJson(responseGroup);
        });


    }

    // this needs to go somewhere else
    private String createNewUuid() {
        String uuid;
        List<String> allIds = groupRepository.getAllIds();
        do {
            uuid = new BigInteger(NUMBER_OF_ID_CHARACTERS * 5, random).toString(32);
        } while (allIds.contains(uuid));
        return uuid;
    }
}
