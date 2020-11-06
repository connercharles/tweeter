package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest {

    private LoginRequest request;
    private LoginResponse expectedResponse;
    private UserDAO mockUserDAO;
    private LoginServiceImpl loginServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new LoginRequest("Ben", "Dover");

        expectedResponse = new LoginResponse(user, null);
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.login(request)).thenReturn(expectedResponse);

        loginServiceImplSpy = Mockito.spy(LoginServiceImpl.class);
        Mockito.when(loginServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceImplSpy.login(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
