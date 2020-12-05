package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Represents an auth token in the system.
        */
public class AuthToken implements Serializable {
    private long date;
    private String token;


    private AuthToken() {}

    public AuthToken(long date, String token){
        this.date = date;
        this.token = token;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
