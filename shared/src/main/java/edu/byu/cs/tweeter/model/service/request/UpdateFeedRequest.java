package edu.byu.cs.tweeter.model.service.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeedRequest {
    private Status status;
    private List<String> followers;

    public UpdateFeedRequest(Status status, List<String> followers){
        this.status = status;
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
