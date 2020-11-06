package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;

public class PostHandler implements RequestHandler<PostRequest, PostResponse> {
    @Override
    public PostResponse handleRequest(PostRequest postRequest, Context context) {
        return getService().postStatus(postRequest);
    }
    public PostServiceImpl getService() { return new PostServiceImpl(); }

}
