package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowService {
    public UnfollowResponse unfollow(UnfollowRequest request) {
        ServerFacade serverFacade = getServerFacade();
        UnfollowResponse unfollowResponse = serverFacade.unfollow(request);

        return unfollowResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }

}
