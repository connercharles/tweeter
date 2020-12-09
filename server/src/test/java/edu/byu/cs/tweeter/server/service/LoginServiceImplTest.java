package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest {

    private LoginRequest request;
    private LoginResponse expectedResponse;
    private LoginServiceImpl loginServiceImplSpy;

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        User user = new User("test", "user2", null);

        request = new LoginRequest("Ben", "Dover");

        expectedResponse = new LoginResponse(user, null);
        loginServiceImplSpy = Mockito.mock(LoginServiceImpl.class);
        Mockito.when(loginServiceImplSpy.login(request)).thenReturn(expectedResponse);

        LoginResponse response = loginServiceImplSpy.login(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testLogin_validData() throws IOException, TweeterRemoteException {
        User user = new User("test", "user2", null);
        AuthTokenDAO authDAO = new AuthTokenDAO();
        String token = authDAO.put();
        AuthToken auth = authDAO.get(token);
        LoginResponse expectedResponse = new LoginResponse(user, auth);

        loginServiceImplSpy = new LoginServiceImpl();
        request = new LoginRequest(user.getAlias(), "poop");

        LoginResponse response = loginServiceImplSpy.login(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }
}
