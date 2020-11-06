package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingServiceProxyTest {
    private IsFollowingRequest validRequest;
    private IsFollowingRequest invalidRequest;

    private IsFollowingResponse failureResponse;
    private IsFollowingResponse successResponse;

    private IsFollowingServiceProxy isFollowingServiceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User userFollows = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup request objects to use in the tests
        validRequest = new IsFollowingRequest(user, userFollows);
        invalidRequest = new IsFollowingRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new IsFollowingResponse(true);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.isFollowing(validRequest, IsFollowingServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new IsFollowingResponse("An exception occurred");
        Mockito.when(mockServerFacade.isFollowing(invalidRequest, IsFollowingServiceProxy.URL_PATH)).thenReturn(failureResponse);

        isFollowingServiceProxy = Mockito.spy(new IsFollowingServiceProxy());
        Mockito.when(isFollowingServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testIsFollowing_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = isFollowingServiceProxy.isFollowing(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testIsFollowing_invalidRequest_returnsNoFollow() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = isFollowingServiceProxy.isFollowing(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
