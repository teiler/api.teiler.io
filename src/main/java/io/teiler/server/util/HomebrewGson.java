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
