package edu.byu.cs.tweeter.server.lambda;

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
import edu.byu.cs.tweeter.server.service.RegisterServiceImpl;

public class RegisterHandlerTest {
    private RegisterRequest request;
    private RegisterResponse expectedResponse;
    private RegisterHandler handlerSpy;
    private RegisterServiceImpl mockRegisterService;

    @BeforeEach
    public void setup() {
        User user1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken auth = new AuthToken(123, "test");

        request = new RegisterRequest("test", "user", "username", "pass", null);
        expectedResponse = new RegisterResponse(user1, auth);

        mockRegisterService = Mockito.mock(RegisterServiceImpl.class);
        Mockito.when(mockRegisterService.register(request)).thenReturn(expectedResponse);

        handlerSpy = Mockito.spy(RegisterHandler.class);
        Mockito.when(handlerSpy.getService()).thenReturn(mockRegisterService);

    }

    @Test
    public void testRegisterHandler() throws IOException, TweeterRemoteException {
        RegisterResponse response = handlerSpy.handleRequest(request, null);
        Assertions.assertEquals(expectedResponse, response);
    }
}
