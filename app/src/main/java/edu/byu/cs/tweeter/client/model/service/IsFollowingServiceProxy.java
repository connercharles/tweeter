package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingServiceProxy implements IsFollowingService {
    static final String URL_PATH = "/isfollowing";

    public IsFollowingResponse isFollowing(IsFollowingRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        IsFollowingResponse followResponse = serverFacade.isFollowing(request, URL_PATH);

        return followResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
