package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class AuthTokenDAOTest {
    private AuthTokenDAO authDAO = new AuthTokenDAO();

    @Test
    public void put(){
        Assertions.assertNotNull(authDAO.put());
    }

    @Test
    public void get(){
        String token = authDAO.put();
        Assertions.assertNotNull(authDAO.get(token));
    }

    @Test
    public void update(){
        String token = authDAO.put();

        Calendar cal = Calendar.getInstance();
        cal.setTime(Calendar.getInstance().getTime());
        cal.add(Calendar.HOUR, -1);
        long time = cal.getTimeInMillis();

        cal.add(Calendar.HOUR, -5);
        long expiredTime = cal.getTimeInMillis();

        authDAO.update(token, time);
        Assertions.assertNotNull(authDAO.get(token));
        authDAO.update(token, expiredTime);
        Assertions.assertNull(authDAO.get(token));
    }

    @Test
    public void delete(){
        String token = authDAO.put();
        authDAO.delete(token);
        Assertions.assertThrows(RuntimeException.class, ()->authDAO.get(token));
    }
}
