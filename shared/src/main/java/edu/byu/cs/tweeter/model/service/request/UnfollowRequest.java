package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest {
    private User user;
    private User unfollows;

    public UnfollowRequest(User user, User unfollows) {
        this.user = user;
        this.unfollows = unfollows;
    }

    private UnfollowRequest() {}

    public void setUser(User user) {
        this.user = user;
    }

    public void setUnfollows(User unfollows) {
        this.unfollows = unfollows;
    }

    public User getUser() {
        return user;
    }

    public User getUnfollows() {
        return unfollows;
    }
}
