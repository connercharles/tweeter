package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;

public class FollowServiceProxyTest {
    private FollowRequest validRequest;
    private FollowRequest invalidRequest;

    private FollowResponse successResponse;
    private FollowResponse failureResponse;

    private FollowServiceProxy followServiceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User userFollows = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowRequest(user, userFollows);
        invalidRequest = new FollowRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.follow(validRequest, FollowServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new FollowResponse("An exception occurred");
        Mockito.when(mockServerFacade.follow(invalidRequest, FollowServiceProxy.URL_PATH)).thenReturn(failureResponse);

        followServiceProxy = Mockito.spy(new FollowServiceProxy());
        Mockito.when(followServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceProxy.follow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testFollow_invalidRequest_returnsNoFollow() throws IOException, TweeterRemoteException {
        FollowResponse response = followServiceProxy.follow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
