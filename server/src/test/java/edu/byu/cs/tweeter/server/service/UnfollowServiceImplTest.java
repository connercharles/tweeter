package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class UnfollowServiceImplTest {
    private UnfollowRequest request;
    private UnfollowResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private UnfollowServiceImpl unfollowServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

//        request = new UnfollowRequest(user1, user2);
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        expectedResponse = new UnfollowResponse();
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
//        Mockito.when(mockFollowingDAO.unfollow(request)).thenReturn(expectedResponse);

        unfollowServiceImplSpy = Mockito.spy(UnfollowServiceImpl.class);
        Mockito.when(unfollowServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);

        UnfollowResponse response = unfollowServiceImplSpy.unfollow(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testUnfollow_validData() throws IOException, TweeterRemoteException {
        unfollowServiceImplSpy = new UnfollowServiceImpl();

        UnfollowResponse response = unfollowServiceImplSpy.unfollow(request);
        Assertions.assertTrue(response.isSuccess());
    }
}
