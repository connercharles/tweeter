package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterService;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

class RegisterPresenterTest {

    private RegisterRequest request;
    private RegisterResponse response;
    private RegisterService mockRegisterService;
    private RegisterPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("Ben", "Dover",
                "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg");

        AuthToken authToken = new AuthToken();
        String firstname = "Ben";
        String lastname = "Dover";
        String username = "testing";
        String password = "passing";


        request = new RegisterRequest(firstname, lastname, username, password, null);
        response = new RegisterResponse(user, authToken);

        // Create a mock RegisterService
        mockRegisterService = Mockito.mock(RegisterService.class);
        Mockito.when(mockRegisterService.register(request)).thenReturn(response);

        // Wrap a RegisterPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {}));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterService);
    }

    @Test
    public void testGetRegister_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockRegisterService.register(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.register(request));
    }

    @Test
    public void testGetRegister_serviceThrowsIOException_presenterThrowsIOException()
            throws IOException, TweeterRemoteException {
        Mockito.when(mockRegisterService.register(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.register(request);
        });
    }
}