package io.teiler.server.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtil {
    
    TimeUtil() { /* intentionally empty */ }

    public static Timestamp convertToTimestamp(LocalDateTime time) {
        if(time == null) {
            return null;
        }
        return Timestamp.valueOf(time);
    }

    public static LocalDateTime convertToLocalDateTime(Timestamp time) {
        if(time == null) {
            return null;
        }
        return time.toLocalDateTime();
    }
    
}
