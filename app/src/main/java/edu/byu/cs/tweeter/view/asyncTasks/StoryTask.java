package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

public class StoryTask extends AsyncTask<StoryRequest, Void, StoryResponse> {

        private final StoryPresenter presenter;
        private final Observer observer;
        private Exception exception;

        public interface Observer {
            void storyRetrieved(StoryResponse storyResponse);
            void handleException(Exception exception);
        }

        public StoryTask(StoryPresenter presenter, StoryTask.Observer observer) {
            if(observer == null) {
                throw new NullPointerException();
            }

            this.presenter = presenter;
            this.observer = observer;
        }

        @Override
        protected StoryResponse doInBackground(StoryRequest... storyRequests) {

            StoryResponse response = null;

            try {
                response = presenter.getStory(storyRequests[0]);
            } catch (IOException ex) {
                exception = ex;
            }

            return response;
        }

        @Override
        protected void onPostExecute(StoryResponse storyResponse) {
            if(exception != null) {
                observer.handleException(exception);
            } else {
                observer.storyRetrieved(storyResponse);
            }
        }
}
