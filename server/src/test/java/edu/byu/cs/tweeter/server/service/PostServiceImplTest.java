package edu.byu.cs.tweeter.server.service;

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
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class PostServiceImplTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private StatusDAO mockStatusDAO;
    private PostServiceImpl postServiceImplSpy;

    @BeforeEach
    public void setup() {
        String message = "so I was like...";

        User author = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken authToken = new AuthToken();
        request = new PostRequest(authToken, author, message);

        expectedResponse = new PostResponse();
        mockStatusDAO = Mockito.mock(StatusDAO.class);
        Mockito.when(mockStatusDAO.postStatus(request)).thenReturn(expectedResponse);

        postServiceImplSpy = Mockito.spy(PostServiceImpl.class);
        Mockito.when(postServiceImplSpy.getStatusDAO()).thenReturn(mockStatusDAO);
    }

    @Test
    public void testPost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PostResponse response = postServiceImplSpy.postStatus(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
