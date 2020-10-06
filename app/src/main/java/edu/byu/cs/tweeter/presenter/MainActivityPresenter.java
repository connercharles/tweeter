package edu.byu.cs.tweeter.presenter;

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

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        LogoutService logoutService = new LogoutService();
        return logoutService.logout(logoutRequest);
    }

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest followNumberRequest) {
        FollowNumberService followNumberService = new FollowNumberService();
        return followNumberService.getFollowNumbers(followNumberRequest);
    }
}
