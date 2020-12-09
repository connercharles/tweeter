package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.service.PostServiceImpl;

public class PostHandlerTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private PostHandler handlerSpy;
    private PostServiceImpl mockPostService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken auth = new AuthToken(123, "test");

        request = new PostRequest(auth, user1, "hi");
        expectedResponse = new PostResponse();

        mockPostService = Mockito.mock(PostServiceImpl.class);
        Mockito.when(mockPostService.postStatus(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(PostHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockPostService);

    }

    @Test
    public void testPostHandler() throws IOException, TweeterRemoteException {
        PostResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
