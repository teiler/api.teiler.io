package io.teiler.server.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

public class TimeUtilTest {
    
    private static final LocalDateTime C99_DATETIME = LocalDateTime.of(1999, 12, 1, 11, 11, 11);
    private static final long MILLIS_SINCE_C99 = 944043071000L;
    
    @Test
    public void testConvertToTimestamp() {
        Timestamp timestamp = new Timestamp(MILLIS_SINCE_C99);
        Assert.assertEquals(timestamp, TimeUtil.convertToTimestamp(C99_DATETIME));
    }
    
    @Test
    public void testConvertToTimestampNull() {
        Assert.assertNull(TimeUtil.convertToTimestamp(null));
    }
    
    @Test
    public void testConvertToLocalDateTime() {
        Timestamp timestamp = new Timestamp(MILLIS_SINCE_C99);
        Assert.assertEquals(C99_DATETIME, TimeUtil.convertToLocalDateTime(timestamp));
    }
    
    @Test
    public void testConvertToLocalDateTimeNull() {
        Assert.assertNull(TimeUtil.convertToLocalDateTime(null));
    }
    
}
