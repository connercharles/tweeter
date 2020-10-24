package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;

public class FollowNumberServiceProxy implements FollowNumberService {
    static final String URL_PATH = "/getfollownumbers";

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        FollowNumberResponse followNumberResponse = serverFacade.getFollowNumbers(request, URL_PATH);

        return followNumberResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
