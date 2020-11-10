package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UnfollowServiceImpl implements UnfollowService {
    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        return getFollowingDAO().unfollow(request);
//        if(request.getUser() != null
//            && request.getUnfollows() != null
//            && getUserDAO().isValidUser(request.getUser())){
//                return getFollowingDAO().unfollow(request);
//        } else {
//            throw new RuntimeException("Bad Request");
//        }

        // TODO: for milestone 4 - add exception stuff
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
