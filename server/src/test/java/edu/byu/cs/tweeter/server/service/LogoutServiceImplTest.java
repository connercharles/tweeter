package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;

public class LogoutServiceImplTest {
    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private AuthTokenDAO mockAuthTokenDAO;
    private LogoutServiceImpl logoutServiceImplSpy;

    @BeforeEach
    public void setup() {
        request = new LogoutRequest(new AuthToken());

        expectedResponse = new LogoutResponse();
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.logout(request)).thenReturn(expectedResponse);

        logoutServiceImplSpy = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(logoutServiceImplSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceImplSpy.logout(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
