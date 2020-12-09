package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowingDAO extends BatchWriter {
    private static String TABLE_NAME = "follows";
    private static String INDEX_NAME = "follows_index";
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static DynamoDB dynamoDB = new DynamoDB(client);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public void addFollowersBatch(List<String> followers, String followTarget) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TABLE_NAME);

        // Add each user into the TableWriteItems object
        for (String user : followers) {
            Item item = new Item()
                    .withPrimaryKey("follower", user)
                    .withString("followee", followTarget);
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
    }

    public void put(String follower, String followee){
        Table table = dynamoDB.getTable(TABLE_NAME);

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("follower", follower,
                            "followee", followee));

            System.out.println("PutFollowing succeeded:\n" + outcome.getPutItemResult());
        }
        catch (Exception e) {
            System.err.println("Unable to add Followee:" + followee + " Follower:" + follower);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to add Followee:" + followee + " Follower:" + follower);
        }
    }

    public Follow get(String follower, String followee){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower", follower,
                "followee", followee);

        try {
            System.out.println("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            System.out.println("GetFollowing succeeded: " + outcome);
            return (new Gson()).fromJson(outcome.toJSON(), Follow.class);
        }
        catch (Exception e) {
            System.err.println("Unable to get Followee:" + followee + " Follower:" + follower);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to get Followee:" + followee + " Follower:" + follower);
        }
    }

    public boolean isFollowing(String follower, String followee){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower", follower,
                "followee", followee);

        try {
            System.out.println("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            System.out.println("GetFollowing succeeded: " + outcome);

            if (outcome == null){
                return false;
            } else {
                return true;
            }
        }
        catch (Exception e) {
            System.err.println("Unable to get Followee:" + followee + " Follower:" + follower);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to get Followee:" + followee + " Follower:" + follower);
        }
    }

    public void delete(String follower, String followee){
        Table table = dynamoDB.getTable(TABLE_NAME);

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("follower", follower,
                        "followee", followee));

        try {
            System.out.println("Attempting a delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteFollowing succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete Followee:" + followee + " Follower:" + follower);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to delete Followee:" + followee + " Follower:" + follower);
        }
    }

    public PagedResults<User> queryByFollowee(String followee, int pageSize, String lastFollower){
        PagedResults<User> result = new PagedResults<>();

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#fr", "follower");

        HashMap<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":follower", new AttributeValue().withS(followee));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#fr = :follower")
                .withExpressionAttributeNames(nameMap)
                .withExpressionAttributeValues(valueMap)
                .withLimit(pageSize);

        if (isNonEmptyString(lastFollower)) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put("followee", new AttributeValue().withS(lastFollower));
            lastKey.put("follower", new AttributeValue().withS(followee));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        }

        try {
            UserDAO userDAO = new UserDAO();

            System.out.println("Followers of " + followee);
            QueryResult queryResult = client.query(queryRequest);
            System.out.println("Made query with results followee: " + queryResult.toString());
            List<Map<String, AttributeValue>> items = queryResult.getItems();
            if (items != null) {
                for (Map<String, AttributeValue> item : items){
                    User user = userDAO.get(item.get("followee").getS());
                    result.addValue(user);
                }
            }

            Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
            if (lastKey != null) {
                result.setLastKey(lastKey.get("followee").getS());
            }

            return result;
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to query Followee:" + followee);
        }
    }

    public PagedResults<User> queryByFollower(String follower, int pageSize, String lastFollowee){
        PagedResults<User> result = new PagedResults<>();

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#fe", "followee");

        HashMap<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":followee", new AttributeValue().withS(follower));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withIndexName(INDEX_NAME)
                .withKeyConditionExpression("#fe = :followee")
                .withExpressionAttributeNames(nameMap)
                .withExpressionAttributeValues(valueMap)
                .withLimit(pageSize);

        if (isNonEmptyString(lastFollowee)) {
            Map<String, AttributeValue> lastKey = new HashMap<>();
            lastKey.put("follower", new AttributeValue().withS(lastFollowee));
            lastKey.put("followee", new AttributeValue().withS(follower));

            queryRequest = queryRequest.withExclusiveStartKey(lastKey);
        }

        try {
            UserDAO userDAO = new UserDAO();

            System.out.println("Followees of " + follower);
            QueryResult queryResult = client.query(queryRequest);
            System.out.println("Made query with results followers: " + queryResult.toString());
            List<Map<String, AttributeValue>> items = queryResult.getItems();
            if (items != null) {
                for (Map<String, AttributeValue> item : items){
                    User user = userDAO.get(item.get("follower").getS());
                    result.addValue(user);
                }
            }

            Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
            if (lastKey != null) {
                result.setLastKey(lastKey.get("follower").getS());
            }

            return result;
        }
        catch (Exception e) {
            System.err.println("Unable to query");
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to query Follower:" + follower);
        }
    }

    public List<String> getAll(String alias) {
        List<String> results = new ArrayList<>();

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#fe", "followee");

        HashMap<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":followee", new AttributeValue().withS(alias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withIndexName(INDEX_NAME)
                .withKeyConditionExpression("#fe = :followee")
                .withExpressionAttributeNames(nameMap)
                .withExpressionAttributeValues(valueMap);

        try {
            System.out.println("get all of " + alias);
            QueryResult queryResult = client.query(queryRequest);
            System.out.println("results of query: " + queryResult.toString());
            List<Map<String, AttributeValue>> items = queryResult.getItems();
            if (items != null) {
                for (Map<String, AttributeValue> item : items){
                    results.add(item.get("follower").getS());
                }
            }

            return results;
        }
        catch (Exception e) {
            System.err.println("Unable to get all");
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to get all followers:" + alias);
        }
    }

}
