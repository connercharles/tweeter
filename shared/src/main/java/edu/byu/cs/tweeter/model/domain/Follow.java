package edu.byu.cs.tweeter.model.domain;

import java.util.Objects;

/**
 * Represents a follow relationship.
 */
public class Follow {

    // TODO: CHANGED FROM USER TO STRINGS!!!
    private final String follower;
    private final String followee;

    public Follow(String follower, String followee) {
        this.follower = follower;
        this.followee = followee;
    }

    public String getFollower() {
        return follower;
    }

    public String getFollowee() {
        return followee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow that = (Follow) o;
        return follower.equals(that.follower) &&
                followee.equals(that.followee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, followee);
    }

    @Override
    public String toString() {
        return "Follow{" +
                "follower=" + follower +
                ", followee=" + followee +
                '}';
    }
}
