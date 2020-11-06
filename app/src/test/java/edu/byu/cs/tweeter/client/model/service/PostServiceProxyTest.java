package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

public class PostServiceProxyTest {
    private PostRequest validRequest;
    private PostRequest invalidRequest;

    private PostResponse successResponse;
    private PostResponse failureResponse;

    private PostServiceProxy loginServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String message = "so I was like...";

        User author = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken authToken = new AuthToken();

        // Setup request objects to use in the tests
        validRequest = new PostRequest(authToken, author, message);
        invalidRequest = new PostRequest(null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new PostResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.postStatus(validRequest, PostServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new PostResponse("An exception occurred");
        Mockito.when(mockServerFacade.postStatus(invalidRequest, PostServiceProxy.URL_PATH)).thenReturn(failureResponse);

        loginServiceProxySpy = Mockito.spy(new PostServiceProxy());
        Mockito.when(loginServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetPost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = loginServiceProxySpy.postStatus(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetPost_invalidRequest_returnsNoPost() throws IOException, TweeterRemoteException {
        PostResponse response = loginServiceProxySpy.postStatus(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
