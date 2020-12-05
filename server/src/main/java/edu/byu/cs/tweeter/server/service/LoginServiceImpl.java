package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImpl extends RequestChecker implements LoginService {

    @Override
    public LoginResponse login(LoginRequest request) {
        checkUser(request.getUsername());
        String hashPassword = getHasher().hash(request.getPassword());

        try{
            User user = getUserDAO().get(request.getUsername());
            String passwordFromDB = getUserDAO().getPassword(request.getUsername());

            String token = getAuthDAO().put();
            AuthToken auth = getAuthDAO().get(token);

            System.out.println("Hash password from db: " + passwordFromDB + "password from request: " + hashPassword);

            // check password
            if (passwordFromDB.equals(hashPassword)){
                return new LoginResponse(user, auth);
            } else {
                throw new RuntimeException("Bad Request : Login Failed, incorrect credentials");
            }
        } catch (Exception ex){
            throw new RuntimeException("Server Error : " + ex.getMessage(), ex);
        }

    }

    UserDAO getUserDAO() { return new UserDAO(); }
    AuthTokenDAO getAuthDAO() { return new AuthTokenDAO(); }
    Hasher getHasher() { return new Hasher(); }
}
