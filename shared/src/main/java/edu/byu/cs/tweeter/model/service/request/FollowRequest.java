package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    private User user;
    private User follows;

    public FollowRequest(User user, User follows) {
        this.user = user;
        this.follows = follows;
    }

    private FollowRequest() {}

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
