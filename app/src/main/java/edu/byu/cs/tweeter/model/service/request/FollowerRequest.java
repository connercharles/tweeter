package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followers for a specified follower.
 */
public class FollowerRequest {

    private final User followee;
    private final int limit;
    private final User lastFollower;

    /**
     * Creates an instance.
     *
     * @param followee the {@link User} whose followers are to be returned.
     * @param limit the maximum number of followers to return.
     * @param lastFollower the last follower that was returned in the previous request (null if
     *                     there was no previous request or if no followers were returned in the
     *                     previous request).
     */
    public FollowerRequest(User followee, int limit, User lastFollower) {
        this.followee = followee;
        this.limit = limit;
        this.lastFollower = lastFollower;
    }

    /**
     * Returns the followee whose followers are to be returned by this request.
     *
     * @return the followee.
     */
    public User getFollowee() {
        return followee;
    }

    /**
     * Returns the number representing the maximum number of followers to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Returns the last follower that was returned in the previous request or null if there was no
     * previous request or if no followers were returned in the previous request.
     *
     * @return the last follower.
     */
    public User getLastFollower() {
        return lastFollower;
    }
}
