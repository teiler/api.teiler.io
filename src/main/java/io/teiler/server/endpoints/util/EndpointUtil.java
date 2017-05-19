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
package io.teiler.server.endpoints.util;

import io.teiler.server.util.Error;
import io.teiler.server.util.HomebrewGson;
import spark.Request;
import spark.Response;

public class EndpointUtil {

    public static final String GROUP_ID_PARAM = ":groupid";
    private static final String LIMIT_PARAM = "limit";
    private static final String ACTIVE_PARAM = "active";

    private EndpointUtil() { /* intentionally empty */ }

    /**
     * Extracts the group ID from the request and normalizes it.
     *
     * @param request The request to extract the group ID from
     * @return The extracted and normalized Group ID
     */
    public static String readGroupId(Request request) {
        String groupId = request.params(GROUP_ID_PARAM);
        return normalizeGroupId(groupId);
    }

    private static String normalizeGroupId(String groupId) {
        return groupId.toLowerCase();
    }

    /**
     * Reads the limit string and sets a default limit if it's not given.
     *
     * @param request The request to extract the limit from
     * @param defaultLimit The limit if a limit is not given
     * @return A limit (given or default)
     */
    public static long readLimit(Request request, long defaultLimit) {
        String limitString = request.queryParams(LIMIT_PARAM);
        long limit = defaultLimit;
        if (limitString != null) {
            limit = Long.parseLong(limitString);
        }
        return limit;
    }

    /**
     * Reads the active string and sets a default state if it's not given.
     *
     * @param request The request to extract the limit from
     * @param defaultState The state it defaults to
     * @return A limit (given or default)
     */
    public static boolean readActive(Request request, boolean defaultState) {
        String activeString = request.queryParams(ACTIVE_PARAM);
        Boolean activeOnly = defaultState;
        if (activeString != null) {
            activeOnly = Boolean.parseBoolean(activeString);
        }
        return activeOnly;
    }

    /**
     * Sets the response so that it represents an error state.
     *
     * @param response The response to modify
     * @param statusCode The HTTP status code to set
     * @param exception The exception to include
     */
    public static void prepareErrorResponse(Response response, int statusCode, Exception exception) {
        response.status(statusCode);
        Error error = new Error(exception.getMessage());
        response.body(HomebrewGson.getInstance().toJson(error));
    }
    
}
