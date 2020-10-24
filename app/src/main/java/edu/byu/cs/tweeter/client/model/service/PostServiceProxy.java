package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostServiceProxy implements PostService {
    static final String URL_PATH = "/poststatus";

    public PostResponse postStatus(PostRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        PostResponse postResponse = serverFacade.postStatus(request, URL_PATH);

        return postResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
