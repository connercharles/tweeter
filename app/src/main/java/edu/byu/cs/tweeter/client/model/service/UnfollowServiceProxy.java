package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceProxy implements UnfollowService {
    static final String URL_PATH = "/unfollow";

    public UnfollowResponse unfollow(UnfollowRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        UnfollowResponse unfollowResponse = serverFacade.unfollow(request, URL_PATH);

        return unfollowResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }

}
