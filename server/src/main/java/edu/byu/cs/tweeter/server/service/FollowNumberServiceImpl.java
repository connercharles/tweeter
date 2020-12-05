package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowNumberServiceImpl  extends RequestChecker implements FollowNumberService {

    @Override
    public FollowNumberResponse getFollowNumbers(FollowNumberRequest request){
        checkUser(request.getUser().getAlias());

        try{
            int followers = getFollowingDAO().getFollowersNumber(request.getUser().getAlias());
            int following = getFollowingDAO().getFollowingNumber(request.getUser().getAlias());
            return new FollowNumberResponse(followers, following);
        } catch (Exception ex){
            throw new RuntimeException("Server Error", ex);
        }
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
