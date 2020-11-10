package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowServiceImplTest {
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private FollowServiceImpl followServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new FollowRequest(user1, user2);

        expectedResponse = new FollowResponse();
    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.follow(request)).thenReturn(expectedResponse);

        followServiceImplSpy = Mockito.spy(FollowServiceImpl.class);
        Mockito.when(followServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);

        FollowResponse response = followServiceImplSpy.follow(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testFollow_validData() throws IOException, TweeterRemoteException {
        followServiceImplSpy = new FollowServiceImpl();

        FollowResponse response = followServiceImplSpy.follow(request);
        Assertions.assertTrue(response.isSuccess());
    }
}
