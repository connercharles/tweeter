package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class FeedServiceProxy implements FeedService {

    static final String URL_PATH = "/getfeed";

    public FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        FeedResponse response = getServerFacade().getFeed(request, URL_PATH);

        if(response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }


    private void loadImages(FeedResponse response) throws IOException {
        for(Status status : response.getFeed()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(status.getAuthor().getImageUrl());
            status.getAuthor().setImageBytes(bytes);
        }
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }

}
