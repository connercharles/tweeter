package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.client.presenter.UserActivityPresenter;
import edu.byu.cs.tweeter.model.service.request.IsFollowingRequest;
import edu.byu.cs.tweeter.model.service.response.IsFollowingResponse;

public class IsFollowingTask extends AsyncTask<IsFollowingRequest, Void, IsFollowingResponse> {
    private final UserActivityPresenter presenter;
    private final IsFollowingTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void isFollowingSuccessful(IsFollowingResponse isFollowingResponse);
        void isFollowingUnsuccessful(IsFollowingResponse isFollowingResponse);
        void handleIsFollowingException(Exception ex);
    }

    public IsFollowingTask(UserActivityPresenter presenter, IsFollowingTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected IsFollowingResponse doInBackground(IsFollowingRequest... isFollowingRequests) {
        IsFollowingResponse isFollowingResponse = null;
        try{
            isFollowingResponse = presenter.isFollowing(isFollowingRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return isFollowingResponse;
    }

    @Override
    protected void onPostExecute(IsFollowingResponse isFollowingResponse) {
        if(exception != null) {
            observer.handleIsFollowingException(exception);
        } else if(isFollowingResponse.isSuccess()) {
            observer.isFollowingSuccessful(isFollowingResponse);
        } else {
            observer.isFollowingUnsuccessful(isFollowingResponse);
        }
    }
}
