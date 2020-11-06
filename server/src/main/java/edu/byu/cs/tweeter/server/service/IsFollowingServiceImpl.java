package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class IsFollowingServiceImpl implements IsFollowingService {

    @Override
    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        return getFollowingDAO().isFollowing(request);
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
