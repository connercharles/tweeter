package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowNumberService;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowNumberServiceImpl implements FollowNumberService {

    public FollowNumberResponse getFollowNumbers(FollowNumberRequest followNumberRequest){
        return getFollowingDAO().getFollowNumbers(followNumberRequest);
    }

    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
