package edu.byu.cs.tweeter.server.service.queueService;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.service.request.UpdateFeedRequest;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class UpdateFeedService {

    public void updateFeed(SQSEvent.SQSMessage msg){
        System.out.println("UpdateFeed: " + msg.toString());
        UpdateFeedRequest request = (new Gson()).fromJson(msg.getBody(), UpdateFeedRequest.class);
        Status status = request.getStatus();

        FeedDAO feedDAO = new FeedDAO();
        System.out.println("Request followers: " + request.getFollowers());
        System.out.println("Request status: " + request.getStatus());

        try{
            feedDAO.putAll(status, request.getFollowers());
        } catch (Exception ex){
            System.out.println("problem with writing: " + ex.toString());
            feedDAO.putAll(status, request.getFollowers());
            System.out.println("try again");
        }

        System.out.println("feed done");
    }

}
