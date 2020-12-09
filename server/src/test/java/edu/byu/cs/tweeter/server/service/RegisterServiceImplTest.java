package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;

public class RegisterServiceImplTest {
    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private RegisterServiceImpl registerServiceImplSpy;

    @BeforeEach
    public void setup() {
        User user = new User("test", "user3", "https://tweeter-profile-pix-con-charles.s3.us-west-2.amazonaws.com/testuser3.png");

        request = new RegisterRequest("test", "user3", "@testuser3",
                "password", "");

        AuthTokenDAO authDAO = new AuthTokenDAO();
        String token = authDAO.put();
        AuthToken auth = authDAO.get(token);

        expectedResponse = new RegisterResponse(user, auth);

    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        registerServiceImplSpy = Mockito.mock(RegisterServiceImpl.class);
        Mockito.when(registerServiceImplSpy.register(request)).thenReturn(expectedResponse);

        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegister_validData() throws IOException, TweeterRemoteException {
        registerServiceImplSpy = new RegisterServiceImpl();

        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }


}
