package io.teiler.server.util.groupid;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomGenerator implements IDGenerator {
    private static SecureRandom random = new SecureRandom();

    public String generateId(int length) {
        final int ENTROPY_BITS_IN_ONE_CHARACTER = 5;
        return new BigInteger(length * ENTROPY_BITS_IN_ONE_CHARACTER, random).toString(32);
    }
}
