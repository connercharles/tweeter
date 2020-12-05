package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class IsFollowingServiceImplTest {

    private IsFollowingRequest request;
    private IsFollowingResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private IsFollowingServiceImpl isFollowingServiceImpl;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new IsFollowingRequest(user1, user2);
    }

    @Test
    public void testIsFollowingFalse_validRequest_correctResponse() throws IOException, TweeterRemoteException {

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new IsFollowingResponse(false);
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
//        Mockito.when(mockFollowingDAO.isFollowing(request)).thenReturn(expectedResponse);

        isFollowingServiceImpl = Mockito.spy(IsFollowingServiceImpl.class);
        Mockito.when(isFollowingServiceImpl.getFollowingDAO()).thenReturn(mockFollowingDAO);

        IsFollowingResponse response = isFollowingServiceImpl.isFollowing(request);
        Assertions.assertEquals(expectedResponse, response);
    }


    @Test
    public void testIsFollowing_validData() throws IOException, TweeterRemoteException {
        isFollowingServiceImpl = new IsFollowingServiceImpl();

        IsFollowingResponse response = isFollowingServiceImpl.isFollowing(request);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.isFollowing());
    }
}
