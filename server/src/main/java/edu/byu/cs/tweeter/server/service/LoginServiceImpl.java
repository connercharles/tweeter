package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImpl implements LoginService {

    @Override
    public LoginResponse login(LoginRequest request) {
        return new LoginResponse(getUserDAO().login(request), getAuthTokenDAO().getAuthToken());
    }

    UserDAO getUserDAO() { return new UserDAO(); }
    AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
