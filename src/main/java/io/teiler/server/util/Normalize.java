package io.teiler.server.util;

public class Normalize {

    private Normalize() { /* intentionally empty */ }
    
    public static String normalizeGroupId(String groupId) {
        return groupId.toLowerCase();
    }

}
