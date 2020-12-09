package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDAOTest {
    private UserDAO dao = new UserDAO();

    @BeforeEach
    public void setup(){
        String url = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
        dao.put("test", "user", "@testuser", url, "password", 0, 0);

    }

    @Test
    public void PutAndGet(){
        Assertions.assertNotNull(dao.get("@testuser"));
        Assertions.assertEquals(dao.get("@testuser").getAlias(), "@testuser");
    }

    @Test
    public void getPassword(){
        Assertions.assertNotNull(dao.getPassword("@testuser"));
        Assertions.assertEquals(dao.getPassword("@testuser"), "password");
    }

    @Test
    public void updateFollowerNum(){
        Assertions.assertEquals(dao.getFollowerNumber("@testuser"), 0);

        dao.updateFollowerNum("@testuser", 1);
        Assertions.assertEquals(dao.getFollowerNumber("@testuser"), 1);
    }

    @Test
    public void updateFollowingNum(){
        Assertions.assertEquals(dao.getFollowingNumber("@testuser"), 0);

        dao.updateFollowingNum("@testuser", 1);
        Assertions.assertEquals(dao.getFollowingNumber("@testuser"), 1);
    }
}
