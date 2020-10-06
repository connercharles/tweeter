package edu.byu.cs.tweeter.model.service.response;

public class FollowResponse extends Response {
    public FollowResponse(String message) {
        super(false, message);
    }

    public FollowResponse() {
        super(true, null);
    }
}
