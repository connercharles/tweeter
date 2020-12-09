package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UnfollowServiceImpl implements UnfollowService {
    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        try{
            getFollowingDAO().delete(request.getUser().getAlias(), request.getUnfollows().getAlias());
            getUserDAO().updateFollowingNum(request.getUser().getAlias(), -1);
            getUserDAO().updateFollowerNum(request.getUnfollows().getAlias(), -1);
            return new UnfollowResponse();
        } catch (Exception ex){
            throw new RuntimeException("Server Error", ex);
        }
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
    UserDAO getUserDAO() {
        return new UserDAO();
    }

}
