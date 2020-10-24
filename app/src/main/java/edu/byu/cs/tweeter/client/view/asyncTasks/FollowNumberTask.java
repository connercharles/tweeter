package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.client.presenter.MainActivityPresenter;
import edu.byu.cs.tweeter.client.presenter.UserActivityPresenter;

public class FollowNumberTask extends AsyncTask<FollowNumberRequest, Void, FollowNumberResponse> {
    private final MainActivityPresenter presenterMain;
    private final UserActivityPresenter presenterFollow;
    private final FollowNumberTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void followNumberSuccessful(FollowNumberResponse followNumberResponse);
        void followNumberUnsuccessful(FollowNumberResponse followNumberResponse);
        void handleFollowNumException(Exception ex);
    }

    public FollowNumberTask(MainActivityPresenter presenterMain, FollowNumberTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenterMain = presenterMain;
        this.observer = observer;
        this.presenterFollow = null;
    }

    public FollowNumberTask(UserActivityPresenter presenterFollow, FollowNumberTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenterMain = null;
        this.presenterFollow = presenterFollow;
        this.observer = observer;
    }

    @Override
    protected FollowNumberResponse doInBackground(FollowNumberRequest... followNumberRequests) {
        FollowNumberResponse followNumberResponse = null;
        try{
            if (this.presenterMain == null){
                followNumberResponse = presenterFollow.getFollowNumbers(followNumberRequests[0]);
            } else {
                followNumberResponse = presenterMain.getFollowNumbers(followNumberRequests[0]);
            }
        } catch (Exception ex) {
            exception = ex;
        }
        return followNumberResponse;
    }

    @Override
    protected void onPostExecute(FollowNumberResponse followNumberResponse) {
        if(exception != null) {
            observer.handleFollowNumException(exception);
        } else if(followNumberResponse.isSuccess()) {
            observer.followNumberSuccessful(followNumberResponse);
        } else {
            observer.followNumberUnsuccessful(followNumberResponse);
        }
    }
}
