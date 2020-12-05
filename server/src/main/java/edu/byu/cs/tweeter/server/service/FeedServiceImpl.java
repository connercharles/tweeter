package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.PagedResults;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FeedServiceImpl extends RequestChecker implements FeedService{

    @Override
    public FeedResponse getFeed(FeedRequest request) {
        checkUser(request.getUser().getAlias());

        try{
            // get list of all those following user
            PagedResults<Status> results = getFeedDAO().queryFeed(request.getUser().getAlias(),
                    request.getLimit(), request.getLastStatus());
            List<Status> feed = results.getValues();

            boolean morePages = false;
            if (results.hasLastKey()){
                morePages = true;
            }

            return new FeedResponse(feed, morePages);
        } catch (Exception ex){
            throw new RuntimeException("Server Error", ex);
        }
    }

    FeedDAO getFeedDAO() { return new FeedDAO(); }
    FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
}
