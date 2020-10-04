package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutService {
    public LogoutResponse logout(LogoutRequest request) {
        ServerFacade serverFacade = getServerFacade();
        LogoutResponse logoutResponse = serverFacade.logout(request);

        return logoutResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
