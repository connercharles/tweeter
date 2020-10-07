package edu.byu.cs.tweeter.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

class ServerFacadeTest {

    private final User user1 = new User("Daffy", "Duck", "");
    private final User user2 = new User("Fred", "Flintstone", "");
    private final User user3 = new User("Barney", "Rubble", "");
    private final User user4 = new User("Wilma", "Rubble", "");
    private final User user5 = new User("Clint", "Eastwood", "");
    private final User user6 = new User("Mother", "Teresa", "");
    private final User user7 = new User("Harriett", "Hansen", "");
    private final User user8 = new User("Zoe", "Zabriski", "");

    private final int FEED_COUNT = 5;
    private final int STORY_COUNT = 20;
    private final Status status1 = new Status("test", user1);
    private final Status status2 = new Status("test", user2);
    private final Status status3 = new Status("test", user3);
    private final Status status4 = new Status("test", user4);
    private final Status status5 = new Status("test", user5);
    private final Status status6 = new Status("test", user6);
    private final Status status7 = new Status("test", user7);

    private final Status status8 = new Status("test0", user8);
    private final Status status81 = new Status("test1", user8);
    private final Status status82 = new Status("test2", user8);
    private final Status status83 = new Status("test3", user8);
    private final Status status84 = new Status("test4", user8);
    private final Status status85 = new Status("test5", user8);
    private final Status status86 = new Status("test6", user8);


    private ServerFacade serverFacadeSpy;

    @BeforeEach
    void setup() {
        serverFacadeSpy = Mockito.spy(new ServerFacade());
    }

    /**
     * FOLLOWING
     */
    @Test
    void testGetFollowees_noFolloweesForUser() {
        List<User> followees = Collections.emptyList();
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followees);

        FollowingRequest request = new FollowingRequest(user1, 10, null);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(0, response.getFollowees().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_oneFollowerForUser_limitGreaterThanUsers() {
        List<User> followees = Collections.singletonList(user2);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followees);

        FollowingRequest request = new FollowingRequest(user1, 10, null);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(1, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_twoFollowersForUser_limitEqualsUsers() {
        List<User> followees = Arrays.asList(user2, user3);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followees);

        FollowingRequest request = new FollowingRequest(user3, 2, null);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_limitLessThanUsers_endsOnPageBoundary() {
        List<User> followees = Arrays.asList(user2, user3, user4, user5, user6, user7);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followees);

        FollowingRequest request = new FollowingRequest(user5, 2, null);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowingRequest(user5, 2, response.getFollowees().get(1));
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user4));
        Assertions.assertTrue(response.getFollowees().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowingRequest(user5, 2, response.getFollowees().get(1));
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user6));
        Assertions.assertTrue(response.getFollowees().contains(user7));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_limitLessThanUsers_notEndsOnPageBoundary() {
        List<User> followees = Arrays.asList(user2, user3, user4, user5, user6, user7, user8);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followees);

        FollowingRequest request = new FollowingRequest(user6, 2, null);
        FollowingResponse response = serverFacadeSpy.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowingRequest(user6, 2, response.getFollowees().get(1));
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user4));
        Assertions.assertTrue(response.getFollowees().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowingRequest(user6, 2, response.getFollowees().get(1));
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user6));
        Assertions.assertTrue(response.getFollowees().contains(user7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FollowingRequest(user6, 2, response.getFollowees().get(1));
        response = serverFacadeSpy.getFollowees(request);

        Assertions.assertEquals(1, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user8));
        Assertions.assertFalse(response.getHasMorePages());
    }

    /**
     * FOLLOWER
     */
    @Test
    void testGetFollowers_noFollowersForUser() {
        List<User> followers = Collections.emptyList();
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user1, 10, null);
        FollowerResponse response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(0, response.getFollowers().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_oneFolloweeForUser_limitGreaterThanUsers() {
        List<User> followers = Collections.singletonList(user2);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user1, 10, null);
        FollowerResponse response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(1, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_twoFolloweesForUser_limitEqualsUsers() {
        List<User> followers = Arrays.asList(user2, user3);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user3, 2, null);
        FollowerResponse response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertTrue(response.getFollowers().contains(user3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_limitLessThanUsers_endsOnPageBoundary() {
        List<User> followers = Arrays.asList(user2, user3, user4, user5, user6, user7);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user5, 2, null);
        FollowerResponse response = serverFacadeSpy.getFollowers(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertTrue(response.getFollowers().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowerRequest(user5, 2, response.getFollowers().get(1));
        response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user4));
        Assertions.assertTrue(response.getFollowers().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowerRequest(user5, 2, response.getFollowers().get(1));
        response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user6));
        Assertions.assertTrue(response.getFollowers().contains(user7));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_limitLessThanUsers_notEndsOnPageBoundary() {
        List<User> followers = Arrays.asList(user2, user3, user4, user5, user6, user7, user8);
        Mockito.when(serverFacadeSpy.getDummyUsers()).thenReturn(followers);

        FollowerRequest request = new FollowerRequest(user6, 2, null);
        FollowerResponse response = serverFacadeSpy.getFollowers(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user2));
        Assertions.assertTrue(response.getFollowers().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowerRequest(user6, 2, response.getFollowers().get(1));
        response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user4));
        Assertions.assertTrue(response.getFollowers().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowerRequest(user6, 2, response.getFollowers().get(1));
        response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(2, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user6));
        Assertions.assertTrue(response.getFollowers().contains(user7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FollowerRequest(user6, 2, response.getFollowers().get(1));
        response = serverFacadeSpy.getFollowers(request);

        Assertions.assertEquals(1, response.getFollowers().size());
        Assertions.assertTrue(response.getFollowers().contains(user8));
        Assertions.assertFalse(response.getHasMorePages());
    }

    /**
     * FEED
     */
    @Test
    void testGetFeed_noFeed() {
        List<Status> feed = Collections.emptyList();
        Mockito.when(serverFacadeSpy.getDummyStatuses(FEED_COUNT)).thenReturn(feed);

        FeedRequest request = new FeedRequest(user1, 10, null);
        FeedResponse response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(0, response.getFeed().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_oneStatus_limitGreaterThanStat() {
        List<Status> feed = Collections.singletonList(status2);
        Mockito.when(serverFacadeSpy.getDummyStatuses(FEED_COUNT)).thenReturn(feed);

        FeedRequest request = new FeedRequest(user1, 10, null);
        FeedResponse response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(1, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_twoStatus_limitEqualsStat() {
        List<Status> feed = Arrays.asList(status2, status3);
        Mockito.when(serverFacadeSpy.getDummyStatuses(FEED_COUNT)).thenReturn(feed);

        FeedRequest request = new FeedRequest(user3, 2, null);
        FeedResponse response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status2));
        Assertions.assertTrue(response.getFeed().contains(status3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitLessThanStat_endsOnPageBoundary() {
        List<Status> feed = Arrays.asList(status2, status3, status4, status5, status6, status7);
        Mockito.when(serverFacadeSpy.getDummyStatuses(FEED_COUNT)).thenReturn(feed);

        FeedRequest request = new FeedRequest(user5, 2, null);
        FeedResponse response = serverFacadeSpy.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status2));
        Assertions.assertTrue(response.getFeed().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(user5, 2, response.getFeed().get(1));
        response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status4));
        Assertions.assertTrue(response.getFeed().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FeedRequest(user5, 2, response.getFeed().get(1));
        response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status6));
        Assertions.assertTrue(response.getFeed().contains(status7));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFeed_limitLessThanStat_notEndsOnPageBoundary() {
        List<Status> feed = Arrays.asList(status2, status3, status4, status5, status6, status7, status8);
        Mockito.when(serverFacadeSpy.getDummyStatuses(FEED_COUNT)).thenReturn(feed);

        FeedRequest request = new FeedRequest(user6, 2, null);
        FeedResponse response = serverFacadeSpy.getFeed(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status2));
        Assertions.assertTrue(response.getFeed().contains(status3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FeedRequest(user6, 2, response.getFeed().get(1));
        response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status4));
        Assertions.assertTrue(response.getFeed().contains(status5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FeedRequest(user6, 2, response.getFeed().get(1));
        response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(2, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status6));
        Assertions.assertTrue(response.getFeed().contains(status7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FeedRequest(user6, 2, response.getFeed().get(1));
        response = serverFacadeSpy.getFeed(request);

        Assertions.assertEquals(1, response.getFeed().size());
        Assertions.assertTrue(response.getFeed().contains(status8));
        Assertions.assertFalse(response.getHasMorePages());
    }

    /**
     * STORY
     */
    @Test
    void testGetStory_noFeed() {
        List<Status> story = Collections.emptyList();
        Mockito.when(serverFacadeSpy.getDummyStatusForUser(STORY_COUNT, user1)).thenReturn(story);

        StoryRequest request = new StoryRequest(user1, 10, null);
        StoryResponse response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(0, response.getStory().size());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_oneStatus_limitGreaterThanStat() {
        List<Status> story = Collections.singletonList(status2);
        Mockito.when(serverFacadeSpy.getDummyStatusForUser(STORY_COUNT, user2)).thenReturn(story);

        StoryRequest request = new StoryRequest(user2, 10, null);
        StoryResponse response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(1, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_twoStatus_limitEqualsStat() {
        List<Status> story = Arrays.asList(status8, status81);
        Mockito.when(serverFacadeSpy.getDummyStatusForUser(STORY_COUNT, user8)).thenReturn(story);

        StoryRequest request = new StoryRequest(user8, 2, null);
        StoryResponse response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(2, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status8));
        Assertions.assertTrue(response.getStory().contains(status81));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_limitLessThanStat_endsOnPageBoundary() {
        List<Status> story = Arrays.asList(status8, status81, status82, status83, status84, status85);
        Mockito.when(serverFacadeSpy.getDummyStatusForUser(STORY_COUNT, user8)).thenReturn(story);

        StoryRequest request = new StoryRequest(user8, 2, null);
        StoryResponse response = serverFacadeSpy.getStory(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status8));
        Assertions.assertTrue(response.getStory().contains(status81));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new StoryRequest(user8, 2, response.getStory().get(1));
        response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(2, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status82));
        Assertions.assertTrue(response.getStory().contains(status83));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new StoryRequest(user8, 2, response.getStory().get(1));
        response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(2, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status84));
        Assertions.assertTrue(response.getStory().contains(status85));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetStory_limitLessThanStat_notEndsOnPageBoundary() {
        List<Status> story = Arrays.asList(status8, status81, status82, status83, status84);
        Mockito.when(serverFacadeSpy.getDummyStatusForUser(STORY_COUNT, user8)).thenReturn(story);

        StoryRequest request = new StoryRequest(user8, 2, null);
        StoryResponse response = serverFacadeSpy.getStory(request);

        // Verify first page
        Assertions.assertEquals(2, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status8));
        Assertions.assertTrue(response.getStory().contains(status81));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new StoryRequest(user8, 2, response.getStory().get(1));
        response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(2, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status82));
        Assertions.assertTrue(response.getStory().contains(status83));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new StoryRequest(user8, 2, response.getStory().get(1));
        response = serverFacadeSpy.getStory(request);

        Assertions.assertEquals(1, response.getStory().size());
        Assertions.assertTrue(response.getStory().contains(status84));
        Assertions.assertFalse(response.getHasMorePages());
    }

    /**
     * LOGIN
     */

    /**
     * LOGOUT
     */

    /**
     * REGISTER
     */

    /**
     * POST
     */

    /**
     * FOLLOW
     */

    /**
     * UNFOLLOW
     */

}