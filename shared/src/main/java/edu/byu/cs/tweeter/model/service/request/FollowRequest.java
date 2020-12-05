package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    private User user;
    private User follows;
    private AuthToken auth;

    public FollowRequest(User user, User follows, AuthToken auth) {
        this.user = user;
        this.follows = follows;
        this.auth = auth;
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

    public AuthToken getAuth() {
        return auth;
    }

    public void setAuth(AuthToken auth) {
        this.auth = auth;
    }
}
