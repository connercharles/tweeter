package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;

public class FollowNumberService {
    public FollowNumberResponse getFollowNumbers(FollowNumberRequest request) {
        ServerFacade serverFacade = getServerFacade();
        FollowNumberResponse followNumberResponse = serverFacade.getFollowNumbers(request);

        return followNumberResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
