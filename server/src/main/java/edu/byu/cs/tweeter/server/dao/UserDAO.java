package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;

public class UserDAO extends BatchWriter {
    private static String TABLE_NAME = "user";
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static DynamoDB dynamoDB = new DynamoDB(client);

    public void addUserBatch(List<User> users, String followTarget) {
        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TABLE_NAME);

        // Add each user into the TableWriteItems object
        for (User user : users) {
            Item item = new Item()
                    .withPrimaryKey("alias", user.getAlias())
                    .withString("firstName", user.getFirstName())
                    .withString("lastName", user.getLastName())
                    .withString("imageUrl", user.getImageUrl())
                    .withString("password", "password")
                    .withInt("follower_number", 0)
                    .withInt("following_number", 1);
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

        updateFollowerNum(followTarget, users.size());
    }

    public void put(String firstname, String lastname, String alias, String imageUrl, String password,
                    int followerNum, int followingNum){
        Table table = dynamoDB.getTable(TABLE_NAME);

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("alias", alias,
                            "firstName", firstname)
                    .with("lastName", lastname)
                    .with("imageUrl", imageUrl)
                    .with("password", password)
                    .withInt("follower_number", followerNum)
                    .withInt("following_number", followingNum));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
        }
        catch (Exception e) {
            System.err.println("Unable to add User: " + alias);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Couldn't put user User: " + alias);
        }
    }

    public User get(String alias){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

        try {
            System.out.println("Attempting to read the User...");
            Item outcome = table.getItem(spec);
            System.out.println("GetUser succeeded: " + outcome);
            return (new Gson()).fromJson(outcome.toJSON(), User.class);
        }
        catch (Exception e) {
            System.err.println("Unable to read User: " + alias);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Couldn't read user item: " + alias);
        }
    }

    public String getPassword(String alias){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);

        try {
            System.out.println("Attempting to read the User...");
            Item outcome = table.getItem(spec);
            System.out.println("GetUser succeeded: " + outcome);
            return outcome.getString("password");
        }
        catch (Exception e) {
            System.err.println("Unable to read User password: " + alias);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Couldn't read user User password: " + alias);
        }
    }

    public int getFollowerNumber(String user){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", user);

        try {
            System.out.println("Attempting to read the User...");
            Item outcome = table.getItem(spec);
            System.out.println("GetFollower Number succeeded: " + outcome);
            return outcome.getInt("follower_number");
        }
        catch (Exception e) {
            System.err.println("Unable to read Follower Number: " + user);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Couldn't read user Follower Number: " + user);
        }
    }

    public int getFollowingNumber(String user){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", user);

        try {
            System.out.println("Attempting to read the User...");
            Item outcome = table.getItem(spec);
            System.out.println("GetFollower Number succeeded: " + outcome);
            return outcome.getInt("following_number");
        }
        catch (Exception e) {
            System.err.println("Unable to read Follower Number: " + user);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Couldn't read user Follower Number: " + user);
        }
    }

    public void updateFollowerNum(String user, int updateByNum){
        Table table = dynamoDB.getTable(TABLE_NAME);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", user)
                .withUpdateExpression("set follower_number = follower_number + :r")
                .withValueMap(new ValueMap().withInt(":r", updateByNum))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the follower number...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("Update follower number succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update follower number: " + user + " " + updateByNum);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to update follower number:" + user + " time:" + updateByNum);
        }
    }

    public void updateFollowingNum(String user, int updateByNum){
        Table table = dynamoDB.getTable(TABLE_NAME);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", user)
                .withUpdateExpression("set following_number = following_number + :r")
                .withValueMap(new ValueMap().withInt(":r", updateByNum))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the following number...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("Update following number succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update following number: " + user + " " + updateByNum);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to update following number:" + user + " time:" + updateByNum);
        }
    }

}
