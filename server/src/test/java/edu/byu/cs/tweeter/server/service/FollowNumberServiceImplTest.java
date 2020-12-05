package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowNumberServiceImplTest {
    private FollowNumberRequest request;
    private FollowNumberResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private FollowNumberServiceImpl followNumberServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new FollowNumberRequest(user);

        expectedResponse = new FollowNumberResponse(7, 5);
    }

    @Test
    public void testGetFollowNumber_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
//        Mockito.when(mockFollowingDAO.getFollowNumbers(request)).thenReturn(expectedResponse);

        followNumberServiceImplSpy = Mockito.spy(FollowNumberServiceImpl.class);
        Mockito.when(followNumberServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);

        FollowNumberResponse response = followNumberServiceImplSpy.getFollowNumbers(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFollowNumber_validData() throws IOException, TweeterRemoteException {
        followNumberServiceImplSpy = new FollowNumberServiceImpl();

        FollowNumberResponse response = followNumberServiceImplSpy.getFollowNumbers(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getFollowerNumber());
    }

}
