package edu.byu.cs.tweeter.server.lambda;

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
import edu.byu.cs.tweeter.server.service.FeedServiceImpl;

public class GetFeedHandlerTest {

    private FeedRequest request;
    private FeedResponse expectedResponse;
    private GetFeedHandler handlerSpy;
    private FeedServiceImpl mockFeedService;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status("test1", user);
        Status status2 = new Status("test2", user);
        Status status3 = new Status("test3", user);

        request = new FeedRequest(user, 3, null);
        expectedResponse = new FeedResponse(Arrays.asList(status1, status2, status3), false);

        mockFeedService = Mockito.mock(FeedServiceImpl.class);
        Mockito.when(mockFeedService.getFeed(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(GetFeedHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockFeedService);

    }

    @Test
    public void testFeedHandler() throws IOException, TweeterRemoteException {
        FeedResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
