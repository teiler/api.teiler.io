package io.teiler.server.util.jsonserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Instant;

public class TimestampSerializer implements JsonSerializer<Timestamp> {
    @Override
    public JsonElement serialize(Timestamp timestamp, Type type, JsonSerializationContext jsonSerializationContext) {
        Instant instant = timestamp.toInstant();
        return new JsonPrimitive(instant.toString());
    }
}

