package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImpl implements RegisterService {

    public RegisterResponse register(RegisterRequest request) {
        return getUserDAO().register(request);
    }

    UserDAO getUserDAO() { return new UserDAO(); }
}
