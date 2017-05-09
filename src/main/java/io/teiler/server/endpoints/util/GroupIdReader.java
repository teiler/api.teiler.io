package io.teiler.server.endpoints.util;

import spark.Request;

public class GroupIdReader {

    private static final String GROUP_ID_PARAM = ":groupid";

    private GroupIdReader() { /* intentionally empty */ }

    public static String getGroupId(Request request) {
        String groupId = request.params(GROUP_ID_PARAM);
        return normalizeGroupId(groupId);
    }

    private static String normalizeGroupId(String groupId) {
        return groupId.toLowerCase();
    }

}
