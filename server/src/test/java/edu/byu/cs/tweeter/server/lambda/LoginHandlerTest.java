package edu.byu.cs.tweeter.server.lambda;

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
import edu.byu.cs.tweeter.server.service.LoginServiceImpl;

public class LoginHandlerTest {

    private LoginRequest request;
    private LoginResponse expectedResponse;
    private LoginHandler handlerSpy;
    private LoginServiceImpl mockLoginService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        request = new LoginRequest("test", "pass");
        expectedResponse = new LoginResponse(user1, new AuthToken());

        mockLoginService = Mockito.mock(LoginServiceImpl.class);
        Mockito.when(mockLoginService.login(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(LoginHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockLoginService);

    }

    @Test
    public void testLoginHandler() throws IOException, TweeterRemoteException {
        LoginResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
