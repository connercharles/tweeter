package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.service.IsFollowingServiceImpl;

public class IsFollowingHandlerTest {

    private IsFollowingRequest request;
    private IsFollowingResponse expectedResponse;
    private IsFollowingHandler handlerSpy;
    private IsFollowingServiceImpl mockIsFollowingService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        request = new IsFollowingRequest(user1, user2);
        expectedResponse = new IsFollowingResponse(false);

        mockIsFollowingService = Mockito.mock(IsFollowingServiceImpl.class);
        Mockito.when(mockIsFollowingService.isFollowing(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(IsFollowingHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockIsFollowingService);

    }

    @Test
    public void testIsFollowingHandler() throws IOException, TweeterRemoteException {
        IsFollowingResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
