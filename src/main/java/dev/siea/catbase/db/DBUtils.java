package dev.siea.catbase.db;

import java.security.SecureRandom;

public class DBUtils {
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateAPIKey(int length) {
        if (length < 16) {
            throw new IllegalArgumentException("API key length must be at least 16 characters.");
        }
        if (length % 8 != 0) {
            throw new IllegalArgumentException("API key length must be divisible by 8.");
        }

        StringBuilder apiKey = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            apiKey.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }

        //dash every 8 characters
        for (int i = 8; i < apiKey.length(); i += 9) {
            apiKey.insert(i, '-');
        }

        return apiKey.toString();
    }
}
