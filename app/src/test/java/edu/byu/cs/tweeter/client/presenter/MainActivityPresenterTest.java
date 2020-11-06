package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

class MainActivityPresenterTest {

    private LogoutRequest requestLogout;
    private LogoutResponse responseLogout;
    private LogoutService mockLogoutService;
    private FollowNumberRequest requestFollow;
    private FollowNumberResponse responseFollow;
    private FollowNumberService mockFollowNumberService;
    private MainActivityPresenter presenter;


    public void logoutSetup() throws IOException, TweeterRemoteException {
        AuthToken authToken = new AuthToken();

        requestLogout = new LogoutRequest(authToken);
        responseLogout = new LogoutResponse();

        mockLogoutService = Mockito.mock(LogoutService.class);
        Mockito.when(mockLogoutService.logout(requestLogout)).thenReturn(responseLogout);

        presenter = Mockito.spy(new MainActivityPresenter(new MainActivityPresenter.View() {}));
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutService);
    }

    public void followNumberSetup() throws IOException, TweeterRemoteException {
        User user = new User("Test", "User", null);

        requestFollow = new FollowNumberRequest(user);
        responseFollow = new FollowNumberResponse(4, 7);

        mockFollowNumberService = Mockito.mock(FollowNumberService.class);
        Mockito.when(mockFollowNumberService.getFollowNumbers(requestFollow)).thenReturn(responseFollow);

        presenter = Mockito.spy(new MainActivityPresenter(new MainActivityPresenter.View() {}));
        Mockito.when(presenter.getFollowNumberService()).thenReturn(mockFollowNumberService);
    }

    @Test
    public void testGetLogout_returnsServiceResult() throws IOException, TweeterRemoteException {
        logoutSetup();

        Mockito.when(mockLogoutService.logout(requestLogout)).thenReturn(responseLogout);

        Assertions.assertEquals(responseLogout, presenter.logout(requestLogout));
    }

    @Test
    public void testGetFollowNumber_returnsServiceResult() throws IOException, TweeterRemoteException {
        followNumberSetup();

        Mockito.when(mockFollowNumberService.getFollowNumbers(requestFollow)).thenReturn(responseFollow);

        Assertions.assertEquals(responseFollow, presenter.getFollowNumbers(requestFollow));
    }


}