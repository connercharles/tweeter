package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.presenter.UserActivityPresenter;

public class FollowTask extends AsyncTask<FollowRequest, Void, FollowResponse> {
    private final UserActivityPresenter presenter;
    private final FollowTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void followSuccessful(FollowResponse followResponse);
        void followUnsuccessful(FollowResponse followResponse);
        void handleFollowException(Exception ex);
    }

    public FollowTask(UserActivityPresenter presenter, FollowTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected FollowResponse doInBackground(FollowRequest... followRequests) {
        FollowResponse followResponse = null;
        followResponse = presenter.follow(followRequests[0]);
        return followResponse;
    }

    @Override
    protected void onPostExecute(FollowResponse followResponse) {
        if(exception != null) {
            observer.handleFollowException(exception);
        } else if(followResponse.isSuccess()) {
            observer.followSuccessful(followResponse);
        } else {
            observer.followUnsuccessful(followResponse);
        }
    }
}
