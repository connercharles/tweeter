package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UserActivityPresenter {
    private final UserActivityPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public UserActivityPresenter(UserActivityPresenter.View view) {
        this.view = view;
    }

    public FollowResponse follow(FollowRequest followRequest) {
        FollowService followService = new FollowService();
        return followService.follow(followRequest);
    }

    public UnfollowResponse unfollow(UnfollowRequest unfollowRequest) {
        UnfollowService unfollowService = new UnfollowService();
        return unfollowService.unfollow(unfollowRequest);
    }

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest followNumberRequest) {
        FollowNumberService followNumberService = new FollowNumberService();
        return followNumberService.getFollowNumbers(followNumberRequest);
    }
}
