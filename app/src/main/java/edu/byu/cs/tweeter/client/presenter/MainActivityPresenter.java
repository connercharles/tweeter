package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowNumberServiceProxy;
import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class MainActivityPresenter {
    private final MainActivityPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public MainActivityPresenter(MainActivityPresenter.View view) {
        this.view = view;
    }

    public LogoutResponse logout(LogoutRequest logoutRequest)
            throws IOException, TweeterRemoteException
    {
        LogoutService logoutService = getLogoutService();
        return logoutService.logout(logoutRequest);
    }

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest followNumberRequest)
            throws IOException, TweeterRemoteException
    {
        FollowNumberService followNumberService = getFollowNumberService();
        return followNumberService.getFollowNumbers(followNumberRequest);
    }

    LogoutService getLogoutService() { return new LogoutServiceProxy(); }
    FollowNumberService getFollowNumberService() { return new FollowNumberServiceProxy(); }
}
