package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowerService;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.PagedResults;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowerServiceImpl extends RequestChecker implements FollowerService {

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        checkUser(request.getFollowee().getAlias());

        try{
            String lastFollower = "";
            if (request.getLastFollower() != null){
                lastFollower = request.getLastFollower().getAlias();
            }

            PagedResults<User> results = getFollowingDAO().queryByFollower(request.getFollowee().getAlias(),
                    request.getLimit(), lastFollower);
            List<User> followers = results.getValues();

            boolean morePages = false;
            if (results.hasLastKey()){
                morePages = true;
            }

            return new FollowerResponse(followers, morePages);
        } catch (Exception ex){
            throw new RuntimeException("Server Error : " + ex.getMessage(), ex);
        }
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
