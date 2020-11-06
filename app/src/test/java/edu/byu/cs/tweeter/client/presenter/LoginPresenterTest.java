package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

class LoginPresenterTest {

    private LoginRequest request;
    private LoginResponse response;
    private LoginService mockLoginService;
    private LoginPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String username = "test";
        String password = "pass";

        User user = new User("Test", "User",
                "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg");

        AuthToken authToken = new AuthToken();

        request = new LoginRequest(username, password);
        response = new LoginResponse(user, authToken);

        // Create a mock LoginService
        mockLoginService = Mockito.mock(LoginService.class);
        Mockito.when(mockLoginService.login(request)).thenReturn(response);

        // Wrap a LoginPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {}));
        Mockito.when(presenter.getLoginService()).thenReturn(mockLoginService);
    }

    @Test
    public void testGetLogin_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLoginService.login(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.login(request));
    }

    @Test
    public void testGetLogin_serviceThrowsIOException_presenterThrowsIOException()
            throws IOException, TweeterRemoteException {
        Mockito.when(mockLoginService.login(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.login(request);
        });
    }
}