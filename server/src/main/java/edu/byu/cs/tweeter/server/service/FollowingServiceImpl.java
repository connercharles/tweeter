package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowingService;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.PagedResults;

public class FollowingServiceImpl extends RequestChecker implements FollowingService {

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) {
        checkUser(request.getFollower().getAlias());

        try{
            String lastFollowee = "";
            if (request.getLastFollowee() != null){
                lastFollowee = request.getLastFollowee().getAlias();
            }

            PagedResults<User> results = getFollowingDAO().queryByFollower(request.getFollower().getAlias(),
                    request.getLimit(), lastFollowee);
            List<User> followers = results.getValues();

            boolean morePages = false;
            if (results.hasLastKey()){
                morePages = true;
            }

            return new FollowingResponse(followers, morePages);
        } catch (Exception ex){
            throw new RuntimeException("Server Error : " + ex.getMessage(), ex);
        }
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
