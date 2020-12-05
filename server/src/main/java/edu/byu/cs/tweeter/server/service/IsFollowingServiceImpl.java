package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.IsFollowingService;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class IsFollowingServiceImpl implements IsFollowingService {

    @Override
    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        boolean isFollowing = getFollowingDAO().isFollowing(request.getUser().getAlias(), request.getFollows().getAlias());
        return new IsFollowingResponse(isFollowing);
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
