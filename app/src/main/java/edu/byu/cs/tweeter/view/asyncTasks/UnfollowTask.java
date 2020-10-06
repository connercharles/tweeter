package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.presenter.UserActivityPresenter;

public class UnfollowTask extends AsyncTask<UnfollowRequest, Void, UnfollowResponse> {
    private final UserActivityPresenter presenter;
    private final UnfollowTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void unfollowSuccessful(UnfollowResponse followResponse);
        void unfollowUnsuccessful(UnfollowResponse followResponse);
        void handleUnfollowException(Exception ex);
    }

    public UnfollowTask(UserActivityPresenter presenter, UnfollowTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected UnfollowResponse doInBackground(UnfollowRequest... unfollowRequests) {
        UnfollowResponse unfollowResponse = null;
        unfollowResponse = presenter.unfollow(unfollowRequests[0]);
        return unfollowResponse;
    }

    @Override
    protected void onPostExecute(UnfollowResponse unfollowResponse) {
        if(exception != null) {
            observer.handleUnfollowException(exception);
        } else if(unfollowResponse.isSuccess()) {
            observer.unfollowSuccessful(unfollowResponse);
        } else {
            observer.unfollowUnsuccessful(unfollowResponse);
        }
    }
}
