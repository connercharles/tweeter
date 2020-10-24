package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowServiceProxy implements FollowService {
    static final String URL_PATH = "/follow";

    public FollowResponse follow(FollowRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        FollowResponse followResponse = serverFacade.follow(request, URL_PATH);

        return followResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
