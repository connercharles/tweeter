package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.service.LogoutService;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenter {
    private final LogoutPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public LogoutPresenter(LogoutPresenter.View view) {
        this.view = view;
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        LogoutService logoutService = new LogoutService();
        return logoutService.logout(logoutRequest);
    }
}
