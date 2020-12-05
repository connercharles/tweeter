package edu.byu.cs.tweeter.server.service;

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
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StoryServiceImplTest {
    private StoryRequest request;
    private StoryResponse expectedResponse;
    private StatusDAO mockStatusDAO;
    private StoryServiceImpl storyServiceImplSpy;

    @BeforeEach
    public void setup() {
        User resultUser = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status("test1", resultUser);
        Status status2 = new Status("test2", resultUser);
        Status status3 = new Status("test3", resultUser);

        request = new StoryRequest(resultUser, 3, null);
        expectedResponse = new StoryResponse(Arrays.asList(status1, status2, status3), false);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        mockStatusDAO = Mockito.mock(StatusDAO.class);
//        Mockito.when(mockStatusDAO.getStory(request)).thenReturn(expectedResponse);

        storyServiceImplSpy = Mockito.spy(StoryServiceImpl.class);
        Mockito.when(storyServiceImplSpy.getStatusDAO()).thenReturn(mockStatusDAO);

        StoryResponse response = storyServiceImplSpy.getStory(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testStory_validData() throws IOException, TweeterRemoteException {
        storyServiceImplSpy = new StoryServiceImpl();

        StoryResponse response = storyServiceImplSpy.getStory(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getStory());
    }
}
