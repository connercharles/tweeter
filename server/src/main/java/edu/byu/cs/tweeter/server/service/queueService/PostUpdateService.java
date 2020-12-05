package edu.byu.cs.tweeter.server.service.queueService;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.UpdateFeedRequest;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class PostUpdateService {
    private static final String UPDATE_QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/345693661661/UpdateQueue";

    public void postUpdate(SQSEvent.SQSMessage msg){
        Status status = (new Gson()).fromJson(msg.getBody(), Status.class);
        System.out.println("Status from sqs post" + status.toString());

        FollowingDAO followingDAO = new FollowingDAO();
        // TODO might be .queryByFollowee
        List<User> followers = followingDAO.queryByFollower(status.getAuthor().getAlias(), 25, null).getValues();

        System.out.println("List of users from follower query" + followers.toString());

        List<String> followerAlias = new ArrayList<>();

        for (User user: followers) {
            followerAlias.add(user.getAlias());
        }

        UpdateFeedRequest request = new UpdateFeedRequest(status, followerAlias);
        String messageBody = (new Gson()).toJson(request);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(UPDATE_QUEUE_URL)
                .withMessageBody(messageBody)
                .withDelaySeconds(5);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
        System.out.println("sqs part 2 request: " + send_msg_request);
        System.out.println("sqs part 2 response: " + send_msg_request);
    }
}
