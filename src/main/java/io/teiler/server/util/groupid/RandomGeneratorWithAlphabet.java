package io.teiler.server.util.groupid;

import java.security.SecureRandom;

public class RandomGeneratorWithAlphabet implements IDGenerator {

    private static final String alphabet = "0123456789abcdefghijkmnpqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    @Override
    public String generateId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}
