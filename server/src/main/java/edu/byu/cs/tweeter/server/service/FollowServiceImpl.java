package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowService;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowServiceImpl extends RequestChecker implements FollowService {

    @Override
    public FollowResponse follow(FollowRequest request) {
        checkAuth(request.getAuth().getToken());

        getFollowingDAO().put(request.getUser().getAlias(), request.getFollows().getAlias());
        getUserDAO().updateFollowingNum(request.getUser().getAlias(), 1);
        getUserDAO().updateFollowerNum(request.getFollows().getAlias(), 1);
        return new FollowResponse();
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
    UserDAO getUserDAO() {
        return new UserDAO();
    }

}
