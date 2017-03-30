package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.exception;

import com.google.gson.Gson;
import io.teiler.server.persistence.repositories.GroupRepository;
import io.teiler.server.util.Error;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalEndpoint implements Endpoint {

    private Gson gson = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalEndpoint.class);

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call to '" + req.pathInfo() + "'"));
        before((req, res) -> res.type("application/json"));

        exception(NotAuthorizedException.class, (e, request, response) -> {
            response.status(401);
            Error error = new Error("NOT_AUTHORIZED_TO_GROUP");
            response.body(gson.toJson(error));
        });

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            Error error = new Error("GENERAL_SERVER_ERROR");
            response.body(gson.toJson(error));
        });

    }

}