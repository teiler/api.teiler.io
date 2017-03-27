package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import com.google.gson.Gson;
import io.teiler.server.dto.Group;
import io.teiler.server.persistence.entities.GroupEntity;
import io.teiler.server.persistence.repositories.GroupRepository;
import java.security.interfaces.ECKey;
import java.util.Random;
import java.util.UUID;
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

            requestGroup.setUuid(new BigInteger(NUMBER_OF_ID_CHARACTERS * 5, random).toString(32));
            LOGGER.debug("New Group: " + requestGroup.getName() + ", " + requestGroup.getUuid());

            GroupEntity groupEntity = groupRepository.create(requestGroup.getUuid(), requestGroup.getName());
            Group responseGroup = new Group(groupEntity.getUuid(), groupEntity.getName());

            return gson.toJson(responseGroup);
        });

        get("/v1/group", (req, res) -> {
            String uuid = req.headers(GROUP_ID_HEADER);
            LOGGER.debug("Request with group ID: " + uuid);
            GroupEntity groupEntity = groupRepository.get(uuid);
            Group responseGroup = new Group(groupEntity.getUuid(), groupEntity.getName());

            return gson.toJson(responseGroup);
        });


    }
}
