package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class UnfollowServiceImpl implements UnfollowService {
    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        try{
            getFollowingDAO().delete(request.getUser().getAlias(), request.getUnfollows().getAlias());
            return new UnfollowResponse();
        } catch (Exception ex){
            throw new RuntimeException("Server Error", ex);
        }
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
