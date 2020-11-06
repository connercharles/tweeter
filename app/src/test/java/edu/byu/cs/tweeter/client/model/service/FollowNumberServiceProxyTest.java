package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;

public class FollowNumberServiceProxyTest {
    private FollowNumberRequest validRequest;
    private FollowNumberRequest invalidRequest;

    private FollowNumberResponse successResponse;
    private FollowNumberResponse failureResponse;

    private FollowNumberServiceProxy followNumberServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        int followerNumber = 4;
        int followingNumber = 7;

        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowNumberRequest(user);
        invalidRequest = new FollowNumberRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowNumberResponse(followerNumber, followingNumber);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFollowNumbers(validRequest, FollowNumberServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new FollowNumberResponse("An exception occurred");
        Mockito.when(mockServerFacade.getFollowNumbers(invalidRequest, FollowNumberServiceProxy.URL_PATH)).thenReturn(failureResponse);

        followNumberServiceProxySpy = Mockito.spy(new FollowNumberServiceProxy());
        Mockito.when(followNumberServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowNumber_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowNumberResponse response = followNumberServiceProxySpy.getFollowNumbers(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowNumber_invalidRequest_returnsNoFollowNumber() throws IOException, TweeterRemoteException {
        FollowNumberResponse response = followNumberServiceProxySpy.getFollowNumbers(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
