package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusDAO {
    private static String TABLE_NAME = "status";
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private final static String POST_QUEUE_URL = "https://sqs.us-west-2.amazonaws.com/345693661661/PostQueue";

    public void put(String authorAlias, String message, long time){
        Table table = dynamoDB.getTable(TABLE_NAME);

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("author", authorAlias,
                            "date", String.valueOf(time))
                    .with("message", message));

            System.out.println("PutStatus succeeded:\n" + outcome.getPutItemResult());

            // add to queue
            UserDAO userDAO = new UserDAO();
            User author = userDAO.get(authorAlias);
            Status status = new Status(message, author, time);
            addToQueue(status);
        }
        catch (Exception e) {
            System.err.println("Unable to add Status: " + authorAlias + ": " + message);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to add Status:" + authorAlias + " message:" + message);
        }
    }

    public Status get(String authorAlias, long time){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("author", authorAlias,
                "date", String.valueOf(time));

        try {
            System.out.println("Attempting to read the Status...");
            Item outcome = table.getItem(spec);
            System.out.println("GetStatus succeeded: " + outcome);

            // get User info
            UserDAO userDAO = new UserDAO();
            User author = userDAO.get(authorAlias);
            long dateTime = Long.parseLong(outcome.getString("date"));

            return new Status(outcome.getString("message"), author, dateTime);
        }
        catch (Exception e) {
            System.err.println("Unable to read Status: " + authorAlias + " " + time);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to get Status:" + authorAlias + " time:" + time);
        }
    }

    public PagedResults<Status> queryByAuthor(String author, int pageSize, Status lastStatus){
        PagedResults<Status> result = new PagedResults<>();

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#ath", "author");

        HashMap<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":author", new AttributeValue().withS(author));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#ath = :author")
                .withExpressionAttributeNames(nameMap)
                .withExpressionAttributeValues(valueMap)
                .withScanIndexForward(false)
                .withLimit(pageSize);

        if (lastStatus != null) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put("date", new AttributeValue().withS(String.valueOf(lastStatus.getWhenPosted())));
            lastKey.put("author", new AttributeValue().withS(lastStatus.getAuthor().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        }

        try {
            System.out.println("Statuses of " + author);
            QueryResult queryResult = client.query(queryRequest);
            List<Map<String, AttributeValue>> items = queryResult.getItems();
            if (items != null) {
                for (Map<String, AttributeValue> item : items){
                    Status status = get(item.get("author").getS(), Long.parseLong(item.get("date").getS()));
                    result.addValue(status);
                }
            }

            Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
            if (lastKey != null) {
                result.setLastKey(lastKey.get("date").getS());
            }

            return result;
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to query by Author:" + author);
        }
    }

    private void addToQueue(Status status){
        String messageBody = (new Gson()).toJson(status);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(POST_QUEUE_URL)
                .withMessageBody(messageBody)
                .withDelaySeconds(5);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
        System.out.println(send_msg_request);
    }

}
