package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowServiceImplTest {
    private FollowRequest request;
    private FollowResponse expectedResponse;
    private FollowServiceImpl followServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user1 = new User("test", "user2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user2 = new User("ben", "dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        AuthTokenDAO authDAO = new AuthTokenDAO();
        String token = authDAO.put();
        AuthToken auth = authDAO.get(token);

        request = new FollowRequest(user1, user2, auth);

        expectedResponse = new FollowResponse();
    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        followServiceImplSpy = Mockito.mock(FollowServiceImpl.class);
        Mockito.when(followServiceImplSpy.follow(request)).thenReturn(expectedResponse);

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
