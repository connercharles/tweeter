package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowerServiceImpl;

public class GetFollowerHandlerTest {
    private FollowerRequest request;
    private FollowerResponse expectedResponse;
    private GetFollowerHandler handlerSpy;
    private FollowerServiceImpl mockFollowerService;

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

        request = new FollowerRequest(user1, 3, null);
        expectedResponse = new FollowerResponse(Arrays.asList(user2, user3, user4), false);

        mockFollowerService = Mockito.mock(FollowerServiceImpl.class);
        Mockito.when(mockFollowerService.getFollowers(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(GetFollowerHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockFollowerService);

    }

    @Test
    public void testFollowerHandler() throws IOException, TweeterRemoteException {
        FollowerResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
