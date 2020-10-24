package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowNumberRequest {
    private final User user;

    public FollowNumberRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
