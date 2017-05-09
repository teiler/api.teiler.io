package io.teiler.server.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtil {

    private TimeUtil() { /* intentionally empty */ }

    /**
     * Converts a {@link LocalDateTime} to a {@link Timestamp}.
     *
     * @param time {@link LocalDateTime}
     * @return {@link Timestamp}
     */
    public static Timestamp convertToTimestamp(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return Timestamp.valueOf(time);
    }

    /**
     * Converts a {@link Timestamp} to a {@link LocalDateTime}.
     *
     * @param time {@link Timestamp}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime convertToLocalDateTime(Timestamp time) {
        if (time == null) {
            return null;
        }
        return time.toLocalDateTime();
    }

}
