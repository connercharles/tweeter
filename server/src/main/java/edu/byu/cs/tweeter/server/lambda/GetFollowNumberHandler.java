package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.server.service.FollowNumberServiceImpl;

public class GetFollowNumberHandler implements RequestHandler<FollowNumberRequest, FollowNumberResponse> {
    @Override
    public FollowNumberResponse handleRequest(FollowNumberRequest followNumberRequest, Context context) {
        FollowNumberServiceImpl service = new FollowNumberServiceImpl();
        return service.getFollowNumbers(followNumberRequest);
    }
}
