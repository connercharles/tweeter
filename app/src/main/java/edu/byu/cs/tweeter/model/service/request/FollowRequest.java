package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    private final User user;
    private final User follows;

    public FollowRequest(User user, User follows) {
        this.user = user;
        this.follows = follows;
    }

    public User getUser() {
        return user;
    }

    public User getFollows() {
        return follows;
    }
}
