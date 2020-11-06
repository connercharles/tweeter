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
import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

class StoryPresenterTest {

    private StoryRequest request;
    private StoryResponse response;
    private StoryService mockStoryService;
    private StoryPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);
        Status status1 = new Status("test", currentUser);
        Status status2 = new Status("test", currentUser);
        Status status3 = new Status("test", currentUser);

        request = new StoryRequest(currentUser, 3, null);
        response = new StoryResponse(Arrays.asList(status1, status2, status3), false);

        // Create a mock StoryService
        mockStoryService = Mockito.mock(StoryService.class);
        Mockito.when(mockStoryService.getStory(request)).thenReturn(response);

        // Wrap a StoryPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStoryService()).thenReturn(mockStoryService);
    }

    @Test
    public void testGetStory_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryService.getStory(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getStory(request));
    }

    @Test
    public void testGetStory_serviceThrowsIOException_presenterThrowsIOException()
            throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryService.getStory(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStory(request);
        });
    }
}