package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.service.UnfollowServiceImpl;

public class UnfollowHandlerTest {
    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private UnfollowHandler handlerSpy;
    private UnfollowServiceImpl mockUnfollowService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken auth = new AuthToken(123, "test");

        request = new UnfollowRequest(user1, user2, auth);
        expectedResponse = new UnfollowResponse();

        mockUnfollowService = Mockito.mock(UnfollowServiceImpl.class);
        Mockito.when(mockUnfollowService.unfollow(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(UnfollowHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockUnfollowService);

    }

    @Test
    public void testUnfollowHandler() throws IOException, TweeterRemoteException {
        UnfollowResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
