package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.ByteArrayInputStream;

public class S3DAO {
    private final static String BUCKET_NAME = "tweeter-profile-pix-con-charles";

    public String put(String fileName, ByteArrayInputStream stream){
        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();

        System.out.format("Uploading to S3 bucket %s...\n", BUCKET_NAME);
        try {
            s3.putObject(BUCKET_NAME, fileName, stream, null);
            return s3.getUrl(BUCKET_NAME, fileName).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Server Error", e);
        }
    }

}
