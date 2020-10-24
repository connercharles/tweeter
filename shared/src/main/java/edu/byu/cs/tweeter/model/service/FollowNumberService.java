package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;

public interface FollowNumberService {
    public FollowNumberResponse getFollowNumbers(FollowNumberRequest request)
            throws IOException, TweeterRemoteException;
}
