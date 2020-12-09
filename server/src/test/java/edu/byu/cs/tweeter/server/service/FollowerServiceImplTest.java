package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowerServiceImplTest {

    private FollowerRequest request;
    private FollowerResponse expectedResponse;
    private FollowerServiceImpl followerServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("test", "user2", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowerRequest(currentUser, 3, null);

        // Setup a mock FollowerDAO that will return known responses
        expectedResponse = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
    }
    
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        followerServiceImplSpy = Mockito.mock(FollowerServiceImpl.class);
        Mockito.when(followerServiceImplSpy.getFollowers(request)).thenReturn(expectedResponse);

        FollowerResponse response = followerServiceImplSpy.getFollowers(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFollowers_validData() throws IOException, TweeterRemoteException {
        followerServiceImplSpy = new FollowerServiceImpl();

        FollowerResponse response = followerServiceImplSpy.getFollowers(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getFollowers());
    }
}
