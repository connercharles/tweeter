package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedDAO extends BatchWriter{
    private static String TABLE_NAME = "feed";

    public PagedResults<Status> queryFeed(String follower, int pageSize, Status lastStatus){
        PagedResults<Status> result = new PagedResults<>();
        StatusDAO statusDAO = new StatusDAO();

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#fr", "follower");

        HashMap<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":follower", new AttributeValue().withS(follower));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#fr = :follower")
                .withExpressionAttributeNames(nameMap)
                .withExpressionAttributeValues(valueMap)
                .withLimit(pageSize);

        if (lastStatus != null) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put("date", new AttributeValue().withS(String.valueOf(lastStatus.getWhenPosted())));
            lastKey.put("follower", new AttributeValue().withS(lastStatus.getAuthor().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        }

        try {
            System.out.println("Statuses of " + follower);
            QueryResult queryResult = client.query(queryRequest);
            List<Map<String, AttributeValue>> items = queryResult.getItems();
            if (items != null) {
                for (Map<String, AttributeValue> item : items){
                    Status status = statusDAO.get(item.get("author").getS(), Long.parseLong(item.get("date").getS()));
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
            throw new RuntimeException("Server Error : Unable to query by Follower:" + follower);
        }
    }

    public void put(String follower, long time, String author){
        Table table = dynamoDB.getTable(TABLE_NAME);

        try {
            System.out.println("Adding a new Feed...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("follower", follower,
                            "date", String.valueOf(time))
                            .with("author", author));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
        }
        catch (Exception e) {
            System.err.println("Unable to add Feed: " + follower + " author: " + author);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to add Feed: " + follower + " author: " + author);
        }
    }

    public void putAll(Status status, List<String> followers){
        TableWriteItems items = new TableWriteItems(TABLE_NAME);

        try {
            System.out.println("Adding a new Feed...");
            // Add each user into the TableWriteItems object
            for (String follower : followers) {
                Item item = new Item()
                        .withPrimaryKey("follower", follower)
                        .withString("date", String.valueOf(status.getWhenPosted()))
                        .withString("author", status.getAuthor().getAlias());
                items.addItemToPut(item);

                // 25 is the maximum number of items allowed in a single batch write.
                // Attempting to write more than 25 items will result in an exception being thrown
                if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                    loopBatchWrite(items);
                    items = new TableWriteItems(TABLE_NAME);
                }
            }

            // Write any leftover items
            if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
                loopBatchWrite(items);
            }

            System.out.println("PutAllFeed succeeded:" + followers.toString());
        }
        catch (Exception e) {
            System.err.println("Unable to add all Feed: " + followers.toString());
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to add all Feed: " + followers.toString());
        }
    }

}
