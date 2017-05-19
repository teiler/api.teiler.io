/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
