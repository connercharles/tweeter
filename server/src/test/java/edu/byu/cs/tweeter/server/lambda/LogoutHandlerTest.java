package edu.byu.cs.tweeter.server.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.LogoutServiceImpl;

public class LogoutHandlerTest {

    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private LogoutHandler handlerSpy;
    private LogoutServiceImpl mockLogoutService;

    @BeforeEach
    public void setup() {
        request = new LogoutRequest(new AuthToken());
        expectedResponse = new LogoutResponse();

        mockLogoutService = Mockito.mock(LogoutServiceImpl.class);
        Mockito.when(mockLogoutService.logout(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(LogoutHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockLogoutService);

    }

    @Test
    public void testLogoutHandler() throws IOException, TweeterRemoteException {
        LogoutResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
