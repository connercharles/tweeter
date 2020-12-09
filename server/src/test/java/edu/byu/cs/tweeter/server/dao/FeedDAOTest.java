package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedDAOTest {
    private FeedDAO dao = new FeedDAO();

    @Test
    public void queryFeed(){
        PagedResults<Status> results = dao.queryFeed("guy1", 5, null);

        Assertions.assertNotNull(results);
        Assertions.assertNotNull(results.getValues());
    }

    @Test
    public void putAll(){
        List<String> data = Arrays.asList("guy1", "guy2", "guy3");
        long time = Calendar.getInstance().getTimeInMillis();
        User author = new User("test", "user2", null);
        Status status = new Status("test putAll", author, time);
    }
}
