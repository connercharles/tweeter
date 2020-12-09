package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Calendar;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.batch.BatchWriter;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.PagedResults;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.service.Hasher;

//public class BatchHandler implements RequestHandler<FeedRequest, void> {
public class BatchHandler {
    public static void main(String[] args){
        UserDAO userDAO = new UserDAO();
        Hasher h = new Hasher();
        String pass = h.hash("poop");
        userDAO.put("Ben", "Dover", "@bendover", "https://i.pinimg.com/originals/50/cb/08/50cb085f28faa563a5e286ecadd3d1bf.jpg", pass, 0, 0);

        BatchWriter bw = new BatchWriter();
        bw.fillDatabase();

    }

////    @Override
//    public void handleRequest(Context context) {
//        getService().fillDatabase();
//    }

    public BatchWriter getService() { return new BatchWriter(); }


    public void warmLambdas(){
        final String n = "nope";
        try{
            AuthTokenDAO authDAO = new AuthTokenDAO();
            authDAO.get("nope");
        } catch (Exception ex){}

        try{
            StatusDAO statusDAO = new StatusDAO();
            statusDAO.get(n, 1);
        } catch (Exception ex){}

        try{
            UserDAO userDAO = new UserDAO();
        } catch (Exception ex){}
        try{
        } catch (Exception ex){}
        try{
        } catch (Exception ex){}
        try{
        } catch (Exception ex){}



        FeedDAO feedDAO = new FeedDAO();

        FollowingDAO followingDAO = new FollowingDAO();

    }



    public void testAuthDAO(){
        AuthTokenDAO authDAO = new AuthTokenDAO();
//        long time = Calendar.getInstance().getTimeInMillis();
        String token1 = authDAO.put();
        String token2 = authDAO.put();
        String token3 = authDAO.put();

        AuthToken auth = authDAO.get(token2);
        auth.getToken();
        authDAO.delete(token1);
        long newTime = Calendar.getInstance().getTimeInMillis();
        authDAO.update(token3, newTime);
        AuthToken auth2 = authDAO.get(token3);
        auth2.getToken();

    }

    public void testUserDAO(){
        getService().fillDatabase();
        UserDAO userDAO = new UserDAO();
        userDAO.put("Ben", "Dover", "@bendover", "www.google.com", "chubbycheeks", 6, 30);
        User user = userDAO.get("@bendover");
        user.getAlias();
    }

    public void testFollowingDAO(){
        getService().fillDatabase();
        FollowingDAO followingDAO = new FollowingDAO();
        followingDAO.put("@hughjazz", "@frankliamphat");
        Follow follow = followingDAO.get("@bendover", "@guy2");
        followingDAO.delete("@bendover", "@guy4");
        follow.getFollowee();

        PagedResults results = followingDAO.queryByFollowee("@guy3", 1, "");
        System.out.println(results.getValues());
        PagedResults results2 = followingDAO.queryByFollower("@bendover", 2, "");
        System.out.println(results2.getValues());
    }

    public void testStatusDAO(){
        StatusDAO statusDAO = new StatusDAO();
        long time = Calendar.getInstance().getTimeInMillis();
        statusDAO.put("@bendover", "hello, it's me.", time);
        Status stat = statusDAO.get("@bendover", time);
        stat.getAuthor();
    }


}
