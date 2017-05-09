package io.teiler.server.util;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class TimeUtilTest {

    private static final LocalDateTime NOW_LOCALDATETIME = LocalDateTime.now(Clock.systemUTC());
    private static final Timestamp NOW_TIMESTAMP = Timestamp.valueOf(NOW_LOCALDATETIME);

    @Test
    public void testConvertToTimestamp() {
        Assert.assertEquals(NOW_TIMESTAMP, TimeUtil.convertToTimestamp(NOW_LOCALDATETIME));
    }

    @Test
    public void testConvertToTimestampNull() {
        Assert.assertNull(TimeUtil.convertToTimestamp(null));
    }

    @Test
    public void testConvertToLocalDateTime() {
        Assert.assertEquals(NOW_LOCALDATETIME, TimeUtil.convertToLocalDateTime(NOW_TIMESTAMP));
    }

    @Test
    public void testConvertToLocalDateTimeNull() {
        Assert.assertNull(TimeUtil.convertToLocalDateTime(null));
    }

}
