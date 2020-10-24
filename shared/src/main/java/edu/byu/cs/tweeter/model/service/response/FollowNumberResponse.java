package edu.byu.cs.tweeter.model.service.response;

public class FollowNumberResponse extends Response {
    private int followerNumber;
    private int followingNumber;

    public FollowNumberResponse(String message) {
        super(false, message);
    }

    public FollowNumberResponse(int followerNumber, int followingNumber) {
        super(true, null);
        this.followerNumber = followerNumber;
        this.followingNumber = followingNumber;
    }

    public int getFollowerNumber() {
        return followerNumber;
    }

    public int getFollowingNumber() {
        return followingNumber;
    }
}
