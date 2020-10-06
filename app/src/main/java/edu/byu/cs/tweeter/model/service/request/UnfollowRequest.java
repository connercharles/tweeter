package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest {
    private final User user;
    private final User unfollows;

    public UnfollowRequest(User user, User unfollows) {
        this.user = user;
        this.unfollows = unfollows;
    }

    public User getUser() {
        return user;
    }

    public User getUnfollows() {
        return unfollows;
    }
}
