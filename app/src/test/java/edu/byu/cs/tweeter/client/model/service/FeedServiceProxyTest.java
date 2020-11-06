package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedServiceProxyTest {

    private FeedRequest validRequest;
    private FeedRequest invalidRequest;

    private FeedResponse successResponse;
    private FeedResponse failureResponse;

    private FeedServiceProxy feedServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status3 = new Status("test3", resultUser2);
        Status status6 = new Status("test6", resultUser2);
        Status status7 = new Status("test7", resultUser2);

        // Setup request objects to use in the tests
        validRequest = new FeedRequest(resultUser2, 3, null);
        invalidRequest = new FeedRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FeedResponse(Arrays.asList(status3, status6, status7), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFeed(validRequest, FeedServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new FeedResponse("An exception occurred");
        Mockito.when(mockServerFacade.getFeed(invalidRequest, FeedServiceProxy.URL_PATH)).thenReturn(failureResponse);

        feedServiceProxySpy = Mockito.spy(new FeedServiceProxy());
        Mockito.when(feedServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceProxySpy.getFeed(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFeed_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceProxySpy.getFeed(validRequest);

        for(Status status : response.getFeed()) {
            Assertions.assertNotNull(status.getAuthor().getImageBytes());
        }
    }

    @Test
    public void testGetFeed_invalidRequest_returnsNoFeed() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceProxySpy.getFeed(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
