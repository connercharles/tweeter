package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class PostServiceImpl implements PostService {

    public PostResponse postStatus(PostRequest postRequest){
        return getStatusDAO().postStatus(postRequest);
    }

    StatusDAO getStatusDAO() { return new StatusDAO(); }
}
