package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;

public class LogoutServiceImplTest {
    private LogoutRequest request;
    private LogoutResponse expectedResponse;
    private LogoutServiceImpl logoutServiceImplSpy;

    @BeforeEach
    public void setup() {
        AuthTokenDAO authDAO = new AuthTokenDAO();
        String token = authDAO.put();
        AuthToken auth = authDAO.get(token);

        request = new LogoutRequest(auth);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        expectedResponse = new LogoutResponse();
        logoutServiceImplSpy = Mockito.mock(LogoutServiceImpl.class);
        Mockito.when(logoutServiceImplSpy.logout(request)).thenReturn(expectedResponse);

        LogoutResponse response = logoutServiceImplSpy.logout(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testLogout_validData() throws IOException, TweeterRemoteException {
        logoutServiceImplSpy = new LogoutServiceImpl();

        LogoutResponse response = logoutServiceImplSpy.logout(request);
        Assertions.assertTrue(response.isSuccess());
    }
}
