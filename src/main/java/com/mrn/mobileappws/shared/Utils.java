package com.mrn.mobileappws.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length) {
        return generateRandomString(length);
    }


    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        // generate a random string value of a given length
        for(int i = 0; i < length; i++) {
            // pick one random char from the alphabet and append it to the string
            // creates a string by iterating multiple times (length) until reaches the length of the alphabet
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }
}
