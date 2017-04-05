package io.teiler.api.service;

import io.teiler.server.util.groupid.RandomGenerator;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Test;

public class GroupServiceTest {

	private static final String ENTROPY_BITS_CONSTANT_NAME = "ENTROPY_BITS_IN_ONE_CHARACTER";
	private static final int ENTROPY_BITS_IN_ONE_CHARACTER_EXPECTED = 5;

	@Test
	public void testThatEntropyBitsAreStillSetToFive() throws NoSuchFieldException, IllegalAccessException {
		Field field = RandomGenerator.class.getDeclaredField(ENTROPY_BITS_CONSTANT_NAME);
		Class<?> fieldType = field.getType();
		field.setAccessible(true); // make sure we can access the value of the private field
		
		Assert.assertEquals(int.class, fieldType);
		Assert.assertEquals("You can't change mathematical facts.", ENTROPY_BITS_IN_ONE_CHARACTER_EXPECTED, field.getInt(null));
	}

}
