package com.chances.chancesuser.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        int keySizeBits = 512; // You can adjust the key size as needed
        byte[] keyBytes = generateSecureKey(keySizeBits);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated Key: " + base64Key);
    }

    private static byte[] generateSecureKey(int keySizeBits) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keySizeBits / 8];
        secureRandom.nextBytes(key);
        return key;
    }
}
