package edu.byu.cs.tweeter.client.view.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.PostTask;

public class Popup extends DialogFragment implements PostPresenter.View, PostTask.Observer {
    private static final String LOG_TAG = "PostStatus";

    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    EditText prompt;
    Button postBtn;
    Button exitBtn;

    private PostPresenter presenter;


    public static Popup newInstance(User user, AuthToken authToken) {
        Popup frag = new Popup();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.popup, container);

        presenter = new PostPresenter(this);

        //noinspection ConstantConditions
        User user = (User) getArguments().getSerializable(USER_KEY);
        AuthToken authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);
        if(authToken == null) {
            throw new RuntimeException("AuthToken not passed to fragment");
        }
        prompt = (EditText) view.findViewById(R.id.text_prompt);
        postBtn = (Button) view.findViewById(R.id.button_post);
        exitBtn = (Button) view.findViewById(R.id.popup_exit);

        // listener for the bottom tab
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostTask postTask = new PostTask(presenter, Popup.this);
                PostRequest request = new PostRequest(authToken, user, prompt.getText().toString());
                postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
                dismiss();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        prompt = (EditText) view.findViewById(R.id.text_prompt);
        // Show soft keyboard automatically and request focus to field
        prompt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void postSuccessful(PostResponse postResponse) {
    }

    @Override
    public void postUnsuccessful(PostResponse postResponse) {
        Toast.makeText(getActivity(), "Failed to post status. " + postResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);

        if(ex instanceof TweeterRemoteException) {
            TweeterRemoteException remoteException = (TweeterRemoteException) ex;
            Log.e(LOG_TAG, "Remote Exception Type: " + remoteException.getRemoteExceptionType());

            Log.e(LOG_TAG, "Remote Stack Trace:");
            if(remoteException.getRemoteStackTrace() != null) {
                for(String stackTraceLine : remoteException.getRemoteStackTrace()) {
                    Log.e(LOG_TAG, "\t\t" + stackTraceLine);
                }
            }
        }

        Toast.makeText(getActivity(), "Failed to post status because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}