package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class IsFollowingRequest {
    private User user;
    private User follows;

    public IsFollowingRequest(User user, User follows) {
        this.user = user;
        this.follows = follows;
    }

    private IsFollowingRequest() {}

    public void setUser(User user) {
        this.user = user;
    }

    public void setFollows(User follows) {
        this.follows = follows;
    }

    public User getUser() {
        return user;
    }

    public User getFollows() {
        return follows;
    }
}
