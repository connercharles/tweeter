package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.service.FollowingServiceImpl;

public class GetFollowingHandlerTest {
    private FollowingRequest request;
    private FollowingResponse expectedResponse;
    private GetFollowingHandler handlerSpy;
    private FollowingServiceImpl mockFollowingService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user4 = new User("FirstName4", "LastName4",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new FollowingRequest(user1, 3, null);
        expectedResponse = new FollowingResponse(Arrays.asList(user2, user3, user4), false);

        mockFollowingService = Mockito.mock(FollowingServiceImpl.class);
        Mockito.when(mockFollowingService.getFollowees(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(GetFollowingHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockFollowingService);

    }

    @Test
    public void testFollowingHandler() throws IOException, TweeterRemoteException {
        FollowingResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
