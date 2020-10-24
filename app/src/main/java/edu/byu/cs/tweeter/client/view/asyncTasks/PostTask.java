package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;

public class PostTask extends AsyncTask<PostRequest, Void, PostResponse> {
    private final PostPresenter presenter;
    private final PostTask.Observer observer;
    private Exception exception;

    public interface Observer {
        void postSuccessful(PostResponse postResponse);
        void postUnsuccessful(PostResponse postResponse);
        void handleException(Exception ex);
    }

    public PostTask(PostPresenter presenter, PostTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected PostResponse doInBackground(PostRequest... postRequests) {
        PostResponse postResponse = null;
        try{
            postResponse = presenter.postStatus(postRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return postResponse;
    }

    @Override
    protected void onPostExecute(PostResponse postResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else if(postResponse.isSuccess()) {
            observer.postSuccessful(postResponse);
        } else {
            observer.postUnsuccessful(postResponse);
        }
    }
}
