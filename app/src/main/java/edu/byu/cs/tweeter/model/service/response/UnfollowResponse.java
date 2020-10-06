package edu.byu.cs.tweeter.model.service.response;

public class UnfollowResponse extends Response{
    public UnfollowResponse(String message) {
        super(false, message);
    }

    public UnfollowResponse() {
        super(true, null);
    }
}
