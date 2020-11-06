package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.server.service.FollowNumberServiceImpl;

public class GetFollowNumberHandlerTest {
    private FollowNumberRequest request;
    private FollowNumberResponse expectedResponse;
    private GetFollowNumberHandler handlerSpy;
    private FollowNumberServiceImpl mockFollowNumberService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new FollowNumberRequest(user1);
        expectedResponse = new FollowNumberResponse(4, 1);

        mockFollowNumberService = Mockito.mock(FollowNumberServiceImpl.class);
        Mockito.when(mockFollowNumberService.getFollowNumbers(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(GetFollowNumberHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockFollowNumberService);

    }

    @Test
    public void testFollowNumberHandler() throws IOException, TweeterRemoteException {
        FollowNumberResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
