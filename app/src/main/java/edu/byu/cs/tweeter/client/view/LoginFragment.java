package edu.byu.cs.tweeter.client.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.LoginTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

public class LoginFragment extends Fragment implements LoginPresenter.View, LoginTask.Observer{
    private static final String LOG_TAG = "LoginFragment";

    EditText usernameTxt;
    EditText passwordTxt;
    Button loginBtn;
    private Toast loginInToast;

    String username;
    String password;
    private LoginPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        presenter = new LoginPresenter(this);

        Button loginButton = root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Makes a login request. The user is hard-coded, so it doesn't matter what data we put
             * in the LoginRequest object.
             *
             * @param view the view object that was clicked.
             */
            @Override
            public void onClick(View view) {
                if (errorCheckInput()){
                    getInput();
                    loginInToast = Toast.makeText(getContext(), "Logging In", Toast.LENGTH_LONG);
                    loginInToast.show();

                    // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                    LoginRequest loginRequest = new LoginRequest(username, password);
                    LoginTask loginTask = new LoginTask(presenter, LoginFragment.this);
                    loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loginRequest);
                } else{
                    Toast.makeText(getContext(), "Please fill out everything", Toast.LENGTH_LONG).show();
                }
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        usernameTxt = (EditText) view.findViewById(R.id.inputUserName);
        passwordTxt = (EditText) view.findViewById(R.id.inputPassword);

    }

    @Override
    public void loginSuccessful(LoginResponse loginResponse) {
        Intent intent = new Intent(getActivity(), MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, loginResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, loginResponse.getAuthToken());

        loginInToast.cancel();
        startActivity(intent);
    }

    @Override
    public void loginUnsuccessful(LoginResponse loginResponse) {
        Toast.makeText(getActivity(), "Failed to login. " + loginResponse.getMessage(), Toast.LENGTH_LONG).show();
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

        Toast.makeText(getActivity(), "Failed to login because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    private boolean errorCheckInput(){
        return !usernameTxt.getText().toString().isEmpty()
                && !passwordTxt.getText().toString().isEmpty();
    }

    private void getInput(){
        username = usernameTxt.getText().toString();
        password = passwordTxt.getText().toString();
    }
}
