package io.teiler.server.endpoints;

import static spark.Spark.before;
import static spark.Spark.exception;

import com.google.gson.Gson;
import io.teiler.server.util.Error;
import io.teiler.server.util.exceptions.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Controller for global things such as authorisation-checks or exception handling.
 *
 * @author lroellin
 */
@Component
public class GlobalEndpointController implements EndpointController {

    public static final String URL_VERSION = "/v1/";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalEndpointController.class);
    private Gson gson = new Gson();

    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call [" + req.requestMethod() + "] to '" + req.pathInfo() + "'"));
        before((req, res) -> res.type("application/json"));

        exception(NotAuthorizedException.class, (e, request, response) -> {
            response.status(401);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            Error error = new Error("GENERAL_SERVER_ERROR");
            LOGGER.error("GENERAL_SERVER_ERROR", e);
            response.body(gson.toJson(error));
        });
    }

}
