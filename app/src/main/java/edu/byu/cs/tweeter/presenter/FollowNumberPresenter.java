package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;

public class FollowNumberPresenter {
    private final FollowNumberPresenter.View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public FollowNumberPresenter(FollowNumberPresenter.View view) {
        this.view = view;
    }

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest followNumberRequest) {
        FollowNumberService followNumberService = new FollowNumberService();
        return followNumberService.getFollowNumbers(followNumberRequest);
    }
}
