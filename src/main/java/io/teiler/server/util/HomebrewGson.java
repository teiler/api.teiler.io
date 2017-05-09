package io.teiler.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.teiler.server.util.jsonserializer.LocalDateTimeSerializer;
import io.teiler.server.util.jsonserializer.TimestampSerializer;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HomebrewGson {

    private static Gson instance;

    private HomebrewGson() { /* intentionally empty */ }

    /**
     * Provides an instance to the singleton GSON.
     *
     * @return the singleton GSON
     */
    public static Gson getInstance() {
        if (instance == null) {
            instance = getHomebrewGson();
        }
        return instance;
    }

    /**
     * Returns a GSON instance with our converters applied.
     *
     * @return a GSON instance with our converters applied
     */
    private static Gson getHomebrewGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeHierarchyAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(Timestamp.class, new TimestampSerializer());
        return gsonBuilder.create();
    }

}
