package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UnfollowService;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class UnfollowServiceImpl implements UnfollowService {
    @Override
    public UnfollowResponse unfollow(UnfollowRequest request) {
        return getFollowingDAO().unfollow(request);
//        new TweeterRemoteException("Bad Request asdlfjsdkfj")
        // TODO
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
