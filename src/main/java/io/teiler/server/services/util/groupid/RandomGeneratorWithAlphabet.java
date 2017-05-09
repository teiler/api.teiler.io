package io.teiler.server.services.util.groupid;

import java.security.SecureRandom;

public class RandomGeneratorWithAlphabet implements IdGenerator {

    // hand tuned to avoid characters that look alike
    private static final String ALPHABET = "0123456789abcdefghijkmnpqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    @Override
    public String generateId(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

}
