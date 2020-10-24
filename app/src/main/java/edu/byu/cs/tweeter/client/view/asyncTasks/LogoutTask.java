package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.MainActivityPresenter;

public class LogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> {
    private final MainActivityPresenter presenter;
    private final LogoutTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void logoutSuccessful(LogoutResponse logoutResponse);
        void logoutUnsuccessful(LogoutResponse logoutResponse);
        void handleLogoutException(Exception ex);
    }

    public LogoutTask(MainActivityPresenter presenter, LogoutTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected LogoutResponse doInBackground(LogoutRequest... logoutRequests) {
        LogoutResponse logoutResponse = null;
        try{
            logoutResponse = presenter.logout(logoutRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return logoutResponse;
    }

    @Override
    protected void onPostExecute(LogoutResponse logoutResponse) {
        if(exception != null) {
            observer.handleLogoutException(exception);
        } else if(logoutResponse.isSuccess()) {
            observer.logoutSuccessful(logoutResponse);
        } else {
            observer.logoutUnsuccessful(logoutResponse);
        }
    }
}
