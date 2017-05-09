package io.teiler.server.util.groupid;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomGenerator implements IdGenerator {

    private static final int ENTROPY_BITS_IN_ONE_CHARACTER = 5;
    private static SecureRandom random = new SecureRandom();

    public String generateId(int length) {
        return new BigInteger(length * ENTROPY_BITS_IN_ONE_CHARACTER, random).toString(32);
    }

}
