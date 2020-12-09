package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class FeedServiceImplTest {
    private FeedRequest request;
    private FeedResponse expectedResponse;
    private FeedServiceImpl feedServiceImplSpy;

    @BeforeEach
    public void setup() {
        User resultUser = new User("test", "user2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status("test1", resultUser);
        Status status2 = new Status("test2", resultUser);
        Status status3 = new Status("test3", resultUser);

        request = new FeedRequest(resultUser, 3, null);

        expectedResponse = new FeedResponse(Arrays.asList(status1, status2, status3), false);
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        feedServiceImplSpy = Mockito.mock(FeedServiceImpl.class);
        Mockito.when(feedServiceImplSpy.getFeed(request)).thenReturn(expectedResponse);

        FeedResponse response = feedServiceImplSpy.getFeed(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFeed_validData() throws IOException, TweeterRemoteException {
        feedServiceImplSpy = new FeedServiceImpl();

        FeedResponse response = feedServiceImplSpy.getFeed(request);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getFeed());
    }
}
