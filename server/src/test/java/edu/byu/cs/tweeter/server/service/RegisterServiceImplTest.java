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
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImplTest {
    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private UserDAO mockUserDAO;
    private RegisterServiceImpl registerServiceImplSpy;

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        User user = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new RegisterRequest("Ben", "Dover", "test", "password", null);

        expectedResponse = new RegisterResponse(user, new AuthToken());
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.register(request)).thenReturn(expectedResponse);

        registerServiceImplSpy = Mockito.spy(RegisterServiceImpl.class);
        Mockito.when(registerServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);

        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    public void testRegister_validData() throws IOException, TweeterRemoteException {
        User user = new User("Ben", "Dover",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new RegisterRequest("Ben", "Dover", "test", "password", null);

        expectedResponse = new RegisterResponse(user, new AuthToken());
        registerServiceImplSpy = new RegisterServiceImpl();

        RegisterResponse response = registerServiceImplSpy.register(request);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(expectedResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }


}
