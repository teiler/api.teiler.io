package io.teiler.server.util.jsonserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type,
        JsonSerializationContext jsonSerializationContext) {
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return new JsonPrimitive(instant.toString());
    }

}

