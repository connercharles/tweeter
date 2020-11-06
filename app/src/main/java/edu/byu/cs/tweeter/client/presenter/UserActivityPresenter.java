package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowNumberServiceProxy;
import edu.byu.cs.tweeter.client.model.service.FollowServiceProxy;
import edu.byu.cs.tweeter.client.model.service.IsFollowingServiceProxy;
import edu.byu.cs.tweeter.client.model.service.UnfollowServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UserActivityPresenter {
    private final UserActivityPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public UserActivityPresenter(UserActivityPresenter.View view) {
        this.view = view;
    }

    public FollowResponse follow(FollowRequest followRequest)
            throws IOException, TweeterRemoteException
    {
        FollowService followService = getFollowService();
        return followService.follow(followRequest);
    }

    public UnfollowResponse unfollow(UnfollowRequest unfollowRequest)
            throws IOException, TweeterRemoteException
    {
        UnfollowService unfollowService = getUnfollowService();
        return unfollowService.unfollow(unfollowRequest);
    }

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest followNumberRequest)
            throws IOException, TweeterRemoteException
    {
        FollowNumberService followNumberService = getFollowNumbersService();
        return followNumberService.getFollowNumbers(followNumberRequest);
    }

    public IsFollowingResponse isFollowing(IsFollowingRequest isFollowingRequest)
            throws IOException, TweeterRemoteException
    {
        IsFollowingService isFollowingService = getIsFollowingService();
        return isFollowingService.isFollowing(isFollowingRequest);
    }

    FollowService getFollowService() { return new FollowServiceProxy(); }
    UnfollowService getUnfollowService() { return new UnfollowServiceProxy(); }
    FollowNumberService getFollowNumbersService() { return new FollowNumberServiceProxy(); }
    IsFollowingService getIsFollowingService() { return new IsFollowingServiceProxy(); }

}
