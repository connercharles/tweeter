package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginServiceProxyTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceProxy loginServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String username = "test";
        String password = "password";

        User user = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken authToken = new AuthToken();

        // Setup request objects to use in the tests
        validRequest = new LoginRequest(username, password);
        invalidRequest = new LoginRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(user, authToken);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.login(validRequest, LoginServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new LoginResponse("An exception occurred");
        Mockito.when(mockServerFacade.login(invalidRequest, LoginServiceProxy.URL_PATH)).thenReturn(failureResponse);

        loginServiceProxySpy = Mockito.spy(new LoginServiceProxy());
        Mockito.when(loginServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException  {
        LoginResponse response = loginServiceProxySpy.login(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetLogin_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceProxySpy.login(validRequest);
        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testGetLogin_invalidRequest_returnsNoLogin() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceProxySpy.login(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
