package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

class FeedPresenterTest {

    private FeedRequest request;
    private FeedResponse response;
    private FeedService mockFeedService;
    private FeedPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status1 = new Status("test", resultUser1);
        Status status2 = new Status("test", resultUser1);
        Status status3 = new Status("test", resultUser1);

        request = new FeedRequest(currentUser, 3, null);
        response = new FeedResponse(Arrays.asList(status1, status2, status3), false);

        // Create a mock FeedService
        mockFeedService = Mockito.mock(FeedService.class);
        Mockito.when(mockFeedService.getFeed(request)).thenReturn(response);

        // Wrap a FeedPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new FeedPresenter(new FeedPresenter.View() {}));
        Mockito.when(presenter.getFeedService()).thenReturn(mockFeedService);
    }

    @Test
    public void testGetFeed_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFeedService.getFeed(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFeed(request));
    }

    @Test
    public void testGetFeed_serviceThrowsIOException_presenterThrowsIOException()
            throws IOException, TweeterRemoteException {
        Mockito.when(mockFeedService.getFeed(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFeed(request);
        });
    }
}