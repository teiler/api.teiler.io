package io.teiler.server.endpoints.util;

import com.google.gson.Gson;
import io.teiler.server.util.Error;
import spark.Request;
import spark.Response;

public class EndpointUtil {

    private static final String GROUP_ID_PARAM = ":groupid";
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


    public static void prepareErrorResponse(Response response, int statusCode, Exception e, Gson gsonInstance) {
        response.status(statusCode);
        Error error = new Error(e.getMessage());
        response.body(gsonInstance.toJson(error));
    }
}
