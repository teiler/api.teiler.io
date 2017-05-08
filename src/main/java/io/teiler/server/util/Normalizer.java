package io.teiler.server.util;

public class Normalizer {

    private Normalizer() { /* intentionally empty */ }
    
    public static String normalizeGroupId(String groupId) {
        return groupId.toLowerCase();
    }

}
