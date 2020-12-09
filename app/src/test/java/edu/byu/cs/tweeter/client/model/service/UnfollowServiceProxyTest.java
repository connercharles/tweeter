package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;

public class UnfollowServiceProxyTest {
    private UnfollowRequest validRequest;
    private UnfollowRequest invalidRequest;

    private UnfollowResponse successResponse;
    private UnfollowResponse failureResponse;

    private UnfollowServiceProxy unfollowServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User userUnfollows = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken auth = new AuthToken(123, "test");

        // Setup request objects to use in the tests
        validRequest = new UnfollowRequest(user, userUnfollows, auth);
        invalidRequest = new UnfollowRequest(null, null, auth);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new UnfollowResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.unfollow(validRequest, UnfollowServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new UnfollowResponse("An exception occurred");
        Mockito.when(mockServerFacade.unfollow(invalidRequest, UnfollowServiceProxy.URL_PATH)).thenReturn(failureResponse);

        unfollowServiceProxySpy = Mockito.spy(new UnfollowServiceProxy());
        Mockito.when(unfollowServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException{
        UnfollowResponse response = unfollowServiceProxySpy.unfollow(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testUnfollow_invalidRequest_returnsNoUnfollow() throws IOException, TweeterRemoteException{
        UnfollowResponse response = unfollowServiceProxySpy.unfollow(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
