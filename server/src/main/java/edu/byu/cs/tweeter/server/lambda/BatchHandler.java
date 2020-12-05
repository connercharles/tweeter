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

//public class BatchHandler implements RequestHandler<FeedRequest, void> {
public class BatchHandler {
    public static void main(String[] args){
//        FollowingDAO fd = new FollowingDAO();
//        fd.put("@bendover", "@guy1");
//        fd.put("@bendover", "@guy2");
//        fd.put("@bendover", "@guy3");
//        fd.put("@bendover", "@guy4");
//        fd.put("@bendover", "@guy5");
//        fd.put("@bendover", "@guy6");



//        System.out.println(fd.isFollowing("@bendover", "@guy1"));

//        UserDAO userDAO = new UserDAO();
//        User user = userDAO.get("@bendover");
//        System.out.println(user.toString());
//        userDAO.put("Ben", "Dover", "@bendover", "https://tweeter-profile-pix-con-charles.s3-us-west-2.amazonaws.com/%40testuser2.png", "pass");

//        System.out.println(userDAO.get("sdlfkj"));

        BatchWriter bw = new BatchWriter();
        bw.fillDatabase();

//        StatusDAO sd = new StatusDAO();
//        sd.put("@guy1", "yo this is @guy1", Calendar.getInstance().getTimeInMillis());
//        sd.put("@guy2", "yo this is @guy2", Calendar.getInstance().getTimeInMillis());
//        sd.put("@guy3", "yo this is @guy3", Calendar.getInstance().getTimeInMillis());
//        sd.put("@guy4", "yo this is @guy4", Calendar.getInstance().getTimeInMillis());
//        sd.put("@guy5", "yo this is @guy5", Calendar.getInstance().getTimeInMillis());
//        sd.put("@guy6", "yo this is @guy6", Calendar.getInstance().getTimeInMillis());
    }

////    @Override
//    public void handleRequest(Context context) {
//        getService().fillDatabase();
//    }

    public BatchWriter getService() { return new BatchWriter(); }


    // TODO: here are test cases
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
//        getService().fillDatabase();
//        UserDAO userDAO = new UserDAO();
////        userDAO.put("Ben", "Dover", "@bendover", "www.google.com", "chubbycheeks");
//        User user = userDAO.get("@bendover");
//        user.getAlias();
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
