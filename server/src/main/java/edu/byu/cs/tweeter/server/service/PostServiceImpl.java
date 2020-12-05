package edu.byu.cs.tweeter.server.service;

import java.util.Calendar;

import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class PostServiceImpl extends RequestChecker implements PostService {

    @Override
    public PostResponse postStatus(PostRequest request){
        checkUser(request.getAuthor().getAlias());
        checkAuth(request.getAuthToken().getToken());

        long time = Calendar.getInstance().getTimeInMillis();

        try{
            getStatusDAO().put(request.getAuthor().getAlias(), request.getMessage(), time);
            return new PostResponse();
        } catch (Exception ex){
            throw new RuntimeException("Server Error", ex);
        }
    }

    StatusDAO getStatusDAO() { return new StatusDAO(); }
}
