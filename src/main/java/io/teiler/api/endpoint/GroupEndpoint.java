package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import com.auth0.jwt.algorithms.Algorithm;
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

/**
 * Created by lroellin on 27.03.17.
 */
@Component
public class GroupEndpoint implements Endpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndpoint.class);
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call to '" + req.pathInfo() + "'"));

        post("/v1/group", (req, res) -> {
            Gson gson = new Gson();
            Group requestGroup = gson.fromJson(req.body(), Group.class);
            LOGGER.debug("New Group: " + requestGroup.getName() + ", " + requestGroup.getUuid());

            requestGroup.setUuid(UUID.randomUUID().toString());

            GroupEntity groupEntity = groupRepository.create(requestGroup.getUuid(), requestGroup.getName());
            Group responseGroup = new Group(groupEntity.getUuid(), groupEntity.getName());

            // Set "secured" to true on HTTPS and false on HTTP
            res.cookie("/", "token", "abc", 86400, false, true);

            LOGGER.debug(req.cookies().toString());

            return gson.toJson(responseGroup);
        });



        // get("/hello/:name", (req, res) -> "Hello " + req.params(":name"));
    }
}
