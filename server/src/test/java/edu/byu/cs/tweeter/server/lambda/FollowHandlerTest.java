package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.service.FollowServiceImpl;

public class FollowHandlerTest {
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowHandler handlerSpy;
    private FollowServiceImpl mockFollowService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        AuthToken auth = new AuthToken(123, "test");

        request = new FollowRequest(user1, user2, auth);
        expectedResponse = new FollowResponse();

        mockFollowService = Mockito.mock(FollowServiceImpl.class);
        Mockito.when(mockFollowService.follow(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(FollowHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockFollowService);

    }

    @Test
    public void testFollowHandler() throws IOException, TweeterRemoteException {
        FollowResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }

}
