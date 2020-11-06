package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostPresenter {
    private final PostPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public PostPresenter(PostPresenter.View view) {
        this.view = view;
    }

    public PostResponse postStatus(PostRequest postRequest)
            throws IOException, TweeterRemoteException
    {
        PostService postService = getPostService();
        return postService.postStatus(postRequest);
    }

    PostService getPostService() {return new PostServiceProxy();}
}
