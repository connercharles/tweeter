package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusDAOTest {
    private StatusDAO dao = new StatusDAO();

    @Test
    public void PutAndGet(){
        String message = "test status put";
        long time = Calendar.getInstance().getTimeInMillis();
        User author = new User("test", "user2", null);
        dao.put(author.getAlias(), message, time);
        Status expectedStatus = new Status(message, author, time);

        Assertions.assertNotNull(dao.get(author.getAlias(), time));
        Assertions.assertEquals(dao.get(author.getAlias(), time), expectedStatus);
    }

    @Test
    public void queryByAuthor(){
        PagedResults<Status> results = dao.queryByAuthor("testuser2", 5, null);

        Assertions.assertNotNull(results);
        Assertions.assertNotNull(results.getValues());
    }
}
