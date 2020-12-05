package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public abstract class RequestChecker {
    public void checkUser(String alias){
        try{
            UserDAO userDAO = new UserDAO();
            userDAO.get(alias);
        } catch(Exception ex){
            throw new RuntimeException("Bad Request", ex);
        }
    }

    public void checkAuth(String token){
        try{
            AuthTokenDAO authDAO = new AuthTokenDAO();
            if(authDAO.get(token) == null){
                throw new RuntimeException("Expired token");
            }
        } catch(Exception ex){
            throw new RuntimeException("Bad Request : " + ex.getMessage(), ex);
        }
    }
}
