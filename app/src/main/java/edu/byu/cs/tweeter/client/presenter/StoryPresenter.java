package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryPresenter {
    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public StoryPresenter(View view) {
        this.view = view;
    }

    public StoryResponse getStory(StoryRequest request)
            throws IOException, TweeterRemoteException
    {
        StoryServiceProxy storyService = getStoryService();
        return storyService.getStory(request);
    }

    StoryServiceProxy getStoryService() {
        return new StoryServiceProxy();
    }
}
