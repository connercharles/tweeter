package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
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

class UserActivityPresenterTest {

    private FollowRequest requestFollow;
    private FollowResponse responseFollow;
    private FollowService mockFollowService;
    private UnfollowRequest requestUnfollow;
    private UnfollowResponse responseUnfollow;
    private UnfollowService mockUnfollowService;
    private FollowNumberRequest requestFollowNum;
    private FollowNumberResponse responseFollowNum;
    private FollowNumberService mockFollowNumberService;
    private IsFollowingRequest requestIsFollowing;
    private IsFollowingResponse responseIsFollowing;
    private IsFollowingService mockIsFollowingService;
    private UserActivityPresenter presenter;

    private final User user = new User("Test", "User", null);
    private final User user2 = new User("Ben", "Dover", null);

    public void followSetup() throws IOException, TweeterRemoteException {
        AuthToken authToken = new AuthToken(123, "test");

        requestFollow = new FollowRequest(user, user2, authToken);
        responseFollow = new FollowResponse();

        mockFollowService = Mockito.mock(FollowService.class);
        Mockito.when(mockFollowService.follow(requestFollow)).thenReturn(responseFollow);

        presenter = Mockito.spy(new UserActivityPresenter(new UserActivityPresenter.View() {}));
        Mockito.when(presenter.getFollowService()).thenReturn(mockFollowService);
    }

    public void unfollowSetup() throws IOException, TweeterRemoteException {
        AuthToken authToken = new AuthToken(123, "test");

        requestUnfollow = new UnfollowRequest(user, user2, authToken);
        responseUnfollow = new UnfollowResponse();

        mockUnfollowService = Mockito.mock(UnfollowService.class);
        Mockito.when(mockUnfollowService.unfollow(requestUnfollow)).thenReturn(responseUnfollow);

        presenter = Mockito.spy(new UserActivityPresenter(new UserActivityPresenter.View() {}));
        Mockito.when(presenter.getUnfollowService()).thenReturn(mockUnfollowService);
    }

    public void followNumberSetup() throws IOException, TweeterRemoteException {
        requestFollowNum = new FollowNumberRequest(user);
        responseFollowNum = new FollowNumberResponse(4, 7);

        mockFollowNumberService = Mockito.mock(FollowNumberService.class);
        Mockito.when(mockFollowNumberService.getFollowNumbers(requestFollowNum)).thenReturn(responseFollowNum);

        presenter = Mockito.spy(new UserActivityPresenter(new UserActivityPresenter.View() {}));
        Mockito.when(presenter.getFollowNumbersService()).thenReturn(mockFollowNumberService);
    }

    public void isFollowingSetup() throws IOException, TweeterRemoteException {
        requestIsFollowing = new IsFollowingRequest(user, user2);
        responseIsFollowing = new IsFollowingResponse(true);

        mockIsFollowingService = Mockito.mock(IsFollowingService.class);
        Mockito.when(mockIsFollowingService.isFollowing(requestIsFollowing)).thenReturn(responseIsFollowing);

        presenter = Mockito.spy(new UserActivityPresenter(new UserActivityPresenter.View() {}));
        Mockito.when(presenter.getIsFollowingService()).thenReturn(mockIsFollowingService);
    }

    @Test
    public void testFollow_returnsServiceResult() throws IOException, TweeterRemoteException {
        followSetup();

        Mockito.when(mockFollowService.follow(requestFollow)).thenReturn(responseFollow);

        Assertions.assertEquals(responseFollow, presenter.follow(requestFollow));
    }

    @Test
    public void testUnfollow_returnsServiceResult() throws IOException, TweeterRemoteException {
        unfollowSetup();

        Mockito.when(mockUnfollowService.unfollow(requestUnfollow)).thenReturn(responseUnfollow);

        Assertions.assertEquals(responseUnfollow, presenter.unfollow(requestUnfollow));
    }

    @Test
    public void testGetFollowNumber_returnsServiceResult() throws IOException, TweeterRemoteException {
        followNumberSetup();

        Mockito.when(mockFollowNumberService.getFollowNumbers(requestFollowNum)).thenReturn(responseFollowNum);

        Assertions.assertEquals(responseFollowNum, presenter.getFollowNumbers(requestFollowNum));
    }

    @Test
    public void testIsFollowing_returnsServiceResult() throws IOException, TweeterRemoteException {
        isFollowingSetup();

        Mockito.when(mockIsFollowingService.isFollowing(requestIsFollowing)).thenReturn(responseIsFollowing);

        Assertions.assertEquals(responseIsFollowing, presenter.isFollowing(requestIsFollowing));
    }
}