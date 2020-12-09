package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;

class FollowingDAOTest {

    private final User user1 = new User("test", "user2", "https://tweeter-profile-pix-con-charles.s3.us-west-2.amazonaws.com/%40testuser2.png");
    private final User user2 = new User("guy1", "guy1", "https://tweeter-profile-pix-con-charles.s3.us-west-2.amazonaws.com/%40guy1.png");

    private FollowingDAO dao = new FollowingDAO();

    @BeforeEach
    void setup() {
        dao = Mockito.spy(new FollowingDAO());
    }

    @Test
    void PutAndGet() {
        dao.put("@testuser2", "@guy1");
        Follow expectedFollow = new Follow("@testuser2", "@guy1");

        Assertions.assertNotNull(dao.get("@testuser2", "@guy1"));
        Assertions.assertEquals(dao.get("@testuser2", "@guy1"), expectedFollow);
    }

    @Test
    void isFollowing() {
        dao.put("@testuser2", "@guy1");
        boolean result = dao.isFollowing("@testuser2", "@guy1");

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }

    @Test
    void delete() {
        dao.put("@testuser2", "@guy1");
        dao.delete("@testuser2", "@guy1");

        Assertions.assertThrows(RuntimeException.class, ()->dao.get("@testuser2", "@guy1"));
    }

    @Test
    void queryByFollowee() {
        PagedResults<User> results = dao.queryByFollowee("@testuser2", 5, null);

        Assertions.assertNotNull(results);
        Assertions.assertNotNull(results.getValues());
    }

    @Test
    void queryByFollower(){
        PagedResults<User> results = dao.queryByFollower("@testuser2", 5, null);

        Assertions.assertNotNull(results);
        Assertions.assertNotNull(results.getValues());
    }

    @Test
    void getAll(){
        List<String> results = dao.getAll("@bendover");

        Assertions.assertNotNull(results);
    }
}
