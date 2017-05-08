package io.teiler.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.teiler.server.util.jsonserializer.LocalDateTimeSerializer;
import io.teiler.server.util.jsonserializer.TimestampSerializer;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class GsonUtil {

    private GsonUtil() { /* intentionally empty */ }

    public static GsonBuilder getHomebrewGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(Timestamp.class, new TimestampSerializer());
        return gsonBuilder;
    }

    public static Gson getHomebrewGson() {
        return getHomebrewGsonBuilder().create();
    }

}
