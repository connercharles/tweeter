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
import edu.byu.cs.tweeter.server.dao.PagedResults;

public class PostUpdateService {
    private static final String UPDATE_QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/345693661661/UpdateQueue";

    public void postUpdate(SQSEvent.SQSMessage msg){
        Status status = (new Gson()).fromJson(msg.getBody(), Status.class);
        System.out.println("Status from sqs post" + status.toString());

        FollowingDAO followingDAO = new FollowingDAO();

        // get list of all followers from dao then sublist that by 25 and send messages as needed.
        List<String> results = followingDAO.getAll(status.getAuthor().getAlias());

        System.out.println("List of users results" + results.toString());

        int i = 0;
        int j = 25;

        if (results.size() <= 25)
            j = results.size();

        while(i < results.size()){
            System.out.println("i: " + i + ", j: " + j);
            List<String> subResult = results.subList(i, j);

            System.out.println("subresult: " + subResult.toString());

            i = j;
            j += 25;

            if (results.size() < j)
                j = results.size();

            UpdateFeedRequest request = new UpdateFeedRequest(status, subResult);
            String messageBody = (new Gson()).toJson(request);

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(UPDATE_QUEUE_URL)
                    .withMessageBody(messageBody);

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
            SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
            System.out.println("sqs part 2 request: " + send_msg_request);
            System.out.println("sqs part 2 response: " + send_msg_result);
        }
    }
}
