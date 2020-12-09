package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutServiceProxyTest {
    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    private LogoutServiceProxy logoutServiceProxySpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        AuthToken authToken = new AuthToken(123, "test");

        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(authToken);
        invalidRequest = new LogoutRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LogoutResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.logout(validRequest, LogoutServiceProxy.URL_PATH)).thenReturn(successResponse);

        failureResponse = new LogoutResponse("An exception occurred");
        Mockito.when(mockServerFacade.logout(invalidRequest, LogoutServiceProxy.URL_PATH)).thenReturn(failureResponse);

        logoutServiceProxySpy = Mockito.spy(new LogoutServiceProxy());
        Mockito.when(logoutServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceProxySpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetLogout_invalidRequest_returnsNoLogout() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceProxySpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
