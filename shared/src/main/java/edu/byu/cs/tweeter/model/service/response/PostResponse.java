package edu.byu.cs.tweeter.model.service.response;

public class PostResponse extends Response {
    public PostResponse(String message) {
        super(false, message);
    }

    public PostResponse() {
        super(true, null);
    }
}
