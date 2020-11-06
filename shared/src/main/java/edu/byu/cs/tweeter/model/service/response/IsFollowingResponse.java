package edu.byu.cs.tweeter.model.service.response;

public class IsFollowingResponse extends Response {
    private boolean following;

    public IsFollowingResponse(String message) {
        super(false, message);
    }

    public IsFollowingResponse(boolean following) {
        super(true, null);
        this.following = following;
    }

    public boolean isFollowing() {
        return following;
    }
}
