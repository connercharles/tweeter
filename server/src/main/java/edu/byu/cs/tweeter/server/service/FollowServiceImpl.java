package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowServiceImpl extends RequestChecker implements FollowService {

    @Override
    public FollowResponse follow(FollowRequest request) {
        checkAuth(request.getAuth().getToken());

        getFollowingDAO().put(request.getUser().getAlias(), request.getFollows().getAlias());
        return new FollowResponse();
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
