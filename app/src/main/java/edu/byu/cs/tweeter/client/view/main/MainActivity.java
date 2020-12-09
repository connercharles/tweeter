package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.MainActivityPresenter;
import edu.byu.cs.tweeter.client.view.StartUpActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowNumberTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View, LogoutTask.Observer,
        FollowNumberTask.Observer {

    private static final String LOG_TAG_FOLLOW = "FollowNumber";
    private static final String LOG_TAG_LOGOUT = "Logout";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    User user;
    private AuthToken authToken;
    private MainActivityPresenter presenter;

    private TextView followeeCount;
    private TextView followerCount;

    @Override
    protected void onResume(){
        super.onResume();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FollowNumberTask followNumberTask = new FollowNumberTask(presenter, MainActivity.this);
        FollowNumberRequest request = new FollowNumberRequest(user);
        followNumberTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Popup popup = Popup.newInstance(user, authToken);
                popup.show(fm, "Popup_Fragment");
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        followeeCount = findViewById(R.id.followeeCount);
        followerCount = findViewById(R.id.followerCount);

        FollowNumberTask followNumberTask = new FollowNumberTask(presenter, MainActivity.this);
        FollowNumberRequest request = new FollowNumberRequest(user);
        followNumberTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LogoutTask logoutTask = new LogoutTask(presenter, MainActivity.this);
                LogoutRequest request = new LogoutRequest(authToken);
                logoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
            }
        });


    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        Intent intent = new Intent(MainActivity.this, StartUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        Toast.makeText(MainActivity.this, "Failed to logout. " + logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleLogoutException(Exception ex) {
        Log.e(LOG_TAG_LOGOUT, ex.getMessage(), ex);

        if(ex instanceof TweeterRemoteException) {
            TweeterRemoteException remoteException = (TweeterRemoteException) ex;
            Log.e(LOG_TAG_LOGOUT, "Remote Exception Type: " + remoteException.getRemoteExceptionType());

            Log.e(LOG_TAG_LOGOUT, "Remote Stack Trace:");
            if(remoteException.getRemoteStackTrace() != null) {
                for(String stackTraceLine : remoteException.getRemoteStackTrace()) {
                    Log.e(LOG_TAG_LOGOUT, "\t\t" + stackTraceLine);
                }
            }
        }

        Toast.makeText(MainActivity.this, "Failed to logout because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void followNumberSuccessful(FollowNumberResponse followNumberResponse) {
        followeeCount.setText(String.valueOf(followNumberResponse.getFollowingNumber()));
        followerCount.setText(String.valueOf(followNumberResponse.getFollowerNumber()));
    }

    @Override
    public void followNumberUnsuccessful(FollowNumberResponse followNumberResponse) {
        Toast.makeText(MainActivity.this, "Failed to get follow numbers. " + followNumberResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleFollowNumException(Exception ex) {
        Log.e(LOG_TAG_FOLLOW, ex.getMessage(), ex);

        if(ex instanceof TweeterRemoteException) {
            TweeterRemoteException remoteException = (TweeterRemoteException) ex;
            Log.e(LOG_TAG_FOLLOW, "Remote Exception Type: " + remoteException.getRemoteExceptionType());

            Log.e(LOG_TAG_FOLLOW, "Remote Stack Trace:");
            if(remoteException.getRemoteStackTrace() != null) {
                for(String stackTraceLine : remoteException.getRemoteStackTrace()) {
                    Log.e(LOG_TAG_FOLLOW, "\t\t" + stackTraceLine);
                }
            }
        }

        Toast.makeText(MainActivity.this, "Failed to get follow numbers because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }


}