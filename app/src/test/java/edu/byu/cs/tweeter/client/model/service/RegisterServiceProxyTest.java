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
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceProxyTest {
    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private RegisterResponse successResponse;
    private RegisterResponse failureResponse;

    private RegisterServiceProxy registerServiceProxySpy;

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        String firstname = "Ben";
        String lastname = "Dover";
        String username = "test";
        String password = "password";

        User user = new User(firstname, lastname,
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken authToken = new AuthToken();

        // Setup request objects to use in the tests
        validRequest = new RegisterRequest(firstname, lastname, username, password, null);
        invalidRequest = new RegisterRequest(null, null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new RegisterResponse(user, authToken);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.register(validRequest, RegisterServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new RegisterResponse("An exception occurred");
        Mockito.when(mockServerFacade.register(invalidRequest, RegisterServiceProxy.URL_PATH)).thenReturn(failureResponse);

        registerServiceProxySpy = Mockito.spy(new RegisterServiceProxy());
        Mockito.when(registerServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterResponse response = registerServiceProxySpy.register(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetRegister_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException{
        RegisterResponse response = registerServiceProxySpy.register(validRequest);
        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testGetRegister_invalidRequest_returnsNoRegister() throws IOException, TweeterRemoteException{
        RegisterResponse response = registerServiceProxySpy.register(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
