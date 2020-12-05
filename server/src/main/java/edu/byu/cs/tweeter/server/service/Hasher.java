package edu.byu.cs.tweeter.server.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Hasher {
    public String hash(String password) {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception ex){
            throw new RuntimeException("Bad Request : could not hash password", ex);
        }
    }
}
