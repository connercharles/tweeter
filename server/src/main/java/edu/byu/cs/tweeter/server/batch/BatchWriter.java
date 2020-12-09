package edu.byu.cs.tweeter.server.batch;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class BatchWriter {
    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;
//    private final static int NUM_USERS = 30;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@bendover";

    public void fillDatabase() {

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAO userDAO = new UserDAO();
        FollowingDAO followDAO = new FollowingDAO();

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String firstname = "Guy " + i;
            String lastname = "Guy " + i;
            String alias = "@guy" + i;
            String imageUrl = "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg";

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User(firstname, lastname, alias, imageUrl);
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            userDAO.addUserBatch(users, FOLLOW_TARGET);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }


}
