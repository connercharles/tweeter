package edu.byu.cs.tweeter.server.lambda.queueLambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.service.queueService.UpdateFeedService;

public class UpdateFeedHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            getService().updateFeed(msg);
        }
        return null;
    }

    UpdateFeedService getService() {return new UpdateFeedService();}


}