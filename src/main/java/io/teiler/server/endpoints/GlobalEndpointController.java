package io.teiler.server.endpoints;

import static spark.Spark.before;
import static spark.Spark.exception;

import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.util.Error;
import io.teiler.server.util.HomebrewGson;
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

    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call [" + req.requestMethod() + "] to '" + req.pathInfo() + "'"));
        before((req, res) -> res.type("application/json"));

        exception(NotAuthorizedException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 401, e));

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            Error error = new Error("GENERAL_SERVER_ERROR");
            LOGGER.error("GENERAL_SERVER_ERROR", e);
            response.body(HomebrewGson.getInstance().toJson(error));
        });
    }

}
