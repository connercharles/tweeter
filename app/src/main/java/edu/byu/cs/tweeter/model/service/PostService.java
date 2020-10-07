package edu.byu.cs.tweeter.model.service;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostService {
    public PostResponse postStatus(PostRequest request) {
        ServerFacade serverFacade = getServerFacade();
        PostResponse postResponse = serverFacade.postStatus(request);

        return postResponse;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
