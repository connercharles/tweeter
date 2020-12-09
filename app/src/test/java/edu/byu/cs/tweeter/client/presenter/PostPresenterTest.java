package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

class PostPresenterTest {
    private PostRequest request;
    private PostResponse response;
    private PostService mockPostService;
    private PostPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User author = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        AuthToken authToken = new AuthToken(123, "test");
        String message = "So there I was...";

        request = new PostRequest(authToken, author, message);
        response = new PostResponse();

        // Create a mock PostService
        mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.postStatus(request)).thenReturn(response);

        // Wrap a PostPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new PostPresenter(new PostPresenter.View() {}));
        Mockito.when(presenter.getPostService()).thenReturn(mockPostService);
    }

    @Test
    public void testGetPost_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockPostService.postStatus(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.postStatus(request));
    }

}