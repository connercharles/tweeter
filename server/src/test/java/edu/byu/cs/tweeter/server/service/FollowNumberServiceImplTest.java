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
    private FollowNumberServiceImpl followNumberServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user = new User("test", "user2", null);

        request = new FollowNumberRequest(user);

        expectedResponse = new FollowNumberResponse(7, 5);
    }

    @Test
    public void testGetFollowNumber_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        followNumberServiceImplSpy = Mockito.mock(FollowNumberServiceImpl.class);
        Mockito.when(followNumberServiceImplSpy.getFollowNumbers(request)).thenReturn(expectedResponse);

        FollowNumberResponse response = followNumberServiceImplSpy.getFollowNumbers(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetFollowNumber_validData() throws IOException, TweeterRemoteException {
        followNumberServiceImplSpy = new FollowNumberServiceImpl();

        FollowNumberResponse response = followNumberServiceImplSpy.getFollowNumbers(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getFollowerNumber());
        Assertions.assertNotNull(response.getFollowingNumber());
    }

}
