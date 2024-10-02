package com._mas1r.licenser.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SerialKey {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateLicenseKey(int LICENSE_KEY_LENGTH) {
        StringBuilder licenseKey = new StringBuilder(LICENSE_KEY_LENGTH);
        for (int i = 0; i < LICENSE_KEY_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            licenseKey.append(CHARACTERS.charAt(index));
        }
        return licenseKey.toString();
    }
}