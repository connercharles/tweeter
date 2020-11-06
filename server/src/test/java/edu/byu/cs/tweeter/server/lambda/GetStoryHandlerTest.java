package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StoryServiceImpl;

public class GetStoryHandlerTest {

    private StoryRequest request;
    private StoryResponse expectedResponse;
    private GetStoryHandler handlerSpy;
    private StoryServiceImpl mockStoryService;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status("test1", user);
        Status status2 = new Status("test2", user);
        Status status3 = new Status("test3", user);

        request = new StoryRequest(user, 3, null);
        expectedResponse = new StoryResponse(Arrays.asList(status1, status2, status3), false);

        mockStoryService = Mockito.mock(StoryServiceImpl.class);
        Mockito.when(mockStoryService.getStory(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(GetStoryHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockStoryService);

    }

    @Test
    public void testStoryHandler() throws IOException, TweeterRemoteException {
        StoryResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
