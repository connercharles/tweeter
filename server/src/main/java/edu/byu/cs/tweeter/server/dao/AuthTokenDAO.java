package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.Calendar;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class AuthTokenDAO {
    private static String TABLE_NAME = "authtoken";
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static DynamoDB dynamoDB = new DynamoDB(client);
    private static final int EXPIRE_TIME = -2; // in hours

    public String put(){
        Table table = dynamoDB.getTable(TABLE_NAME);
        String token = UUID.randomUUID().toString().substring(0,10);
        long time = Calendar.getInstance().getTimeInMillis();

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("token", token,
                            "date", String.valueOf(time)));

            System.out.println("PutAuth succeeded:\n" + outcome.getPutItemResult());
            return token;
        }
        catch (Exception e) {
            System.err.println("Unable to add Auth: " + token + ": " + time);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to put Auth:" + token + " time:" + time);
        }
    }

    public AuthToken get(String token){
        Table table = dynamoDB.getTable(TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("token", token);

        try {
            System.out.println("Attempting to read the Auth...");
            Item outcome = table.getItem(spec);
            System.out.println("GetAuth succeeded: " + outcome);

            long expirationTime = getExpirationTime();
            long lastTime = Long.parseLong(outcome.getString("date"));
            System.out.println("Expiration time: " + expirationTime + ", last time: " + lastTime);

            if (lastTime >= expirationTime){
                long newTime = Calendar.getInstance().getTimeInMillis();
                System.out.println("updating token to new time: " + newTime);
                update(token, newTime);
                return new AuthToken(newTime, outcome.getString("token"));
            } else {
                delete(token);
                return null;
            }

        }
        catch (Exception e) {
            System.err.println("Unable to read Auth: " + token);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to get item:" + token);
        }
    }

    public void update(String token, long newTime){
        Table table = dynamoDB.getTable(TABLE_NAME);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("token", token)
                .withUpdateExpression("set #dt = :r")
                .withNameMap(new NameMap().with("#dt", "date"))
                .withValueMap(new ValueMap().withString(":r", String.valueOf(newTime)))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the Auth...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateAuth succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update Auth: " + token + " " + newTime);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to update Auth:" + token + " time:" + newTime);
        }
    }

    public void delete(String token){
        Table table = dynamoDB.getTable(TABLE_NAME);

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("token", token));

        try {
            System.out.println("Attempting a delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteAuth succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete Auth: " + token);
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error : Unable to delete Auth:" + token);
        }
    }

    private long getExpirationTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(Calendar.getInstance().getTime());
        cal.add(Calendar.HOUR, EXPIRE_TIME);
        return cal.getTimeInMillis();
    }
}
