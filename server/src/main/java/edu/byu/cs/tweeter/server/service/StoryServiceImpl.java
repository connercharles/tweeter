package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.PagedResults;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StoryServiceImpl extends RequestChecker implements StoryService {
    @Override
    public StoryResponse getStory(StoryRequest request) {
        checkUser(request.getUser().getAlias());

        try{
            PagedResults<Status> results = getStatusDAO().queryByAuthor(request.getUser().getAlias(),
                    request.getLimit(), request.getLastStatus());
            List<Status> story = results.getValues();

            boolean morePages = false;
            if (results.hasLastKey()){
                morePages = true;
            }

            return new StoryResponse(story, morePages);
        } catch (Exception ex){
            throw new RuntimeException("Server Error", ex);
        }
    }

    StatusDAO getStatusDAO() { return new StatusDAO(); }
}
