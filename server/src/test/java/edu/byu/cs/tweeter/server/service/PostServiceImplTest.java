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
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class PostServiceImplTest {
    private PostRequest request;
    private PostResponse expectedResponse;
    private PostServiceImpl postServiceImplSpy;

    @BeforeEach
    public void setup() {
        String message = "so I was like...";

        User author = new User("test", "user2", null);
        AuthTokenDAO authDAO = new AuthTokenDAO();
        String token = authDAO.put();
        AuthToken auth = authDAO.get(token);

        request = new PostRequest(auth, author, message);
    }

    @Test
    public void testPost_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        expectedResponse = new PostResponse();
        postServiceImplSpy = Mockito.mock(PostServiceImpl.class);
        Mockito.when(postServiceImplSpy.postStatus(request)).thenReturn(expectedResponse);

        PostResponse response = postServiceImplSpy.postStatus(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testPost_validData() throws IOException, TweeterRemoteException {
        postServiceImplSpy = new PostServiceImpl();

        PostResponse response = postServiceImplSpy.postStatus(request);
        Assertions.assertTrue(response.isSuccess());
    }
}
