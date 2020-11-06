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
    private UnfollowServiceImpl followServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new UnfollowRequest(user1, user2);

        expectedResponse = new UnfollowResponse();
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.unfollow(request)).thenReturn(expectedResponse);

        followServiceImplSpy = Mockito.spy(UnfollowServiceImpl.class);
        Mockito.when(followServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
    }

    @Test
    public void testUnfollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowResponse response = followServiceImplSpy.unfollow(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
