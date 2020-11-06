package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryServiceProxyTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;

    private StoryResponse successResponse;
    private StoryResponse failureResponse;

    private StoryServiceProxy storyServiceProxySpy;

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status status1 = new Status("test1", currentUser);
        Status status2 = new Status("test2", currentUser);
        Status status3 = new Status("test3", currentUser);

        // Setup request objects to use in the tests
        validRequest = new StoryRequest(currentUser, 3, null);
        invalidRequest = new StoryRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new StoryResponse(Arrays.asList(status1, status2, status3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStory(validRequest, StoryServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new StoryResponse("An exception occurred");
        Mockito.when(mockServerFacade.getStory(invalidRequest, StoryServiceProxy.URL_PATH)).thenReturn(failureResponse);

        storyServiceProxySpy = Mockito.spy(new StoryServiceProxy());
        Mockito.when(storyServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceProxySpy.getStory(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetStory_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException{
        StoryResponse response = storyServiceProxySpy.getStory(validRequest);

        for(Status status : response.getStory()) {
            Assertions.assertNotNull(status.getAuthor().getImageBytes());
        }
    }

    @Test
    public void testGetStory_invalidRequest_returnsNoStory() throws IOException, TweeterRemoteException{
        StoryResponse response = storyServiceProxySpy.getStory(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
