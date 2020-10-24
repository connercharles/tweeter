package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class AuthTokenDAO {

    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse();
    }
    public AuthToken getAuthToken() { return new AuthToken(); }
}
