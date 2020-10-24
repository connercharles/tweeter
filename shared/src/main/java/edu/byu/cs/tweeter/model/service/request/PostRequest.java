package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class PostRequest {
    private AuthToken authToken;
    private User author;
    private String message;

    public PostRequest(AuthToken authToken, User author, String message) {
        this.authToken = authToken;
        this.author = author;
        this.message = message;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public User getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
