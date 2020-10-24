package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowNumberRequest;
import edu.byu.cs.tweeter.model.service.request.FollowRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.service.response.FollowNumberResponse;
import edu.byu.cs.tweeter.model.service.response.FollowResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowResponse;
import edu.byu.cs.tweeter.client.presenter.UserActivityPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowNumberTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.following.FollowerFragment;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity implements FollowNumberTask.Observer,
        FollowTask.Observer, UnfollowTask.Observer, UserActivityPresenter.View {
    private static final String LOG_TAG_FOLLOW_NUM = "FollowNumber";
    private static final String LOG_TAG_FOLLOW = "Follow";
    private static final String LOG_TAG_UNFOLLOW = "Unfollow";

    public static final String CLICKED_USER = "ClickedUser";
    public static final String MAIN_USER = "MainUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String FOLLOW_STRING = "Follow";
    public static final String FOLLOWING_STRING = "Following";

    private static final int STORY_FRAGMENT_POSITION = 0;
    private static final int FOLLOWING_FRAGMENT_POSITION = 1;
    private static final int FOLLOWER_FRAGMENT_POSITION = 2;

    private UserActivityPresenter presenter;
    public static User mainUser;
    public static User clickedUser;

    private TextView followeeCount;
    private TextView followerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        presenter = new UserActivityPresenter(this);

        clickedUser = (User) getIntent().getSerializableExtra(CLICKED_USER);
        mainUser = (User) getIntent().getSerializableExtra(MAIN_USER);
        if(clickedUser == null || mainUser == null) {
            throw new RuntimeException("User(s) not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        TextView userName = findViewById(R.id.userPgName);
        userName.setText(clickedUser.getName());

        TextView userAlias = findViewById(R.id.userPgAlias);
        userAlias.setText(clickedUser.getAlias());

        ImageView userImageView = findViewById(R.id.userPgImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(clickedUser.getImageBytes()));

        followeeCount = findViewById(R.id.userPgFolloweeCount);
        followerCount = findViewById(R.id.userPgFollowerCount);

        FollowNumberTask followNumberTask = new FollowNumberTask(presenter, UserActivity.this);
        FollowNumberRequest request = new FollowNumberRequest(clickedUser);
        followNumberTask.execute(request);

        Button followBtn = findViewById(R.id.userPgFollowButton);
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followBtn.getText().equals(FOLLOW_STRING)){
                    FollowTask followTask = new FollowTask(presenter, UserActivity.this);
                    FollowRequest request = new FollowRequest(mainUser, clickedUser);
                    followTask.execute(request);

                    followBtn.setText(FOLLOWING_STRING);
                    followBtn.setBackgroundColor(Color.WHITE);
                    followBtn.setTextColor(Color.BLACK);
                } else{
                    UnfollowTask unfollowTask = new UnfollowTask(presenter, UserActivity.this);
                    UnfollowRequest request = new UnfollowRequest(mainUser, clickedUser);
                    unfollowTask.execute(request);

                    followBtn.setText(FOLLOW_STRING);
                    followBtn.setBackgroundColor(Color.parseColor("#D81B60"));
                    followBtn.setTextColor(Color.WHITE);
                }
            }
        });

        if (mainUser.equals(clickedUser)){
            followBtn.setVisibility(View.INVISIBLE);
            followBtn.setClickable(false);
        } else{
            followBtn.setVisibility(View.VISIBLE);
            followBtn.setClickable(true);
        }


        TabLayout tabs = findViewById(R.id.userPgTabs);
        TabLayout.Tab storyTab = tabs.newTab();
        storyTab.setText("Story");
        tabs.addTab(storyTab, STORY_FRAGMENT_POSITION, true);
        TabLayout.Tab followingTab = tabs.newTab();
        followingTab.setText("Following");
        tabs.addTab(followingTab, FOLLOWING_FRAGMENT_POSITION);
        TabLayout.Tab followersTab = tabs.newTab();
        followersTab.setText("Followers");
        tabs.addTab(followersTab, FOLLOWER_FRAGMENT_POSITION);

        FragmentManager fm = this.getSupportFragmentManager();
        StoryFragment storyFragment = StoryFragment.newInstance(clickedUser, authToken);
        fm.beginTransaction()
                .add(R.id.userPgFragmentViewer, storyFragment)
                .commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();

                switch (tab.getPosition()){
                    case STORY_FRAGMENT_POSITION:
                        StoryFragment storyFragment = StoryFragment.newInstance(clickedUser, authToken);
                        fm.beginTransaction()
                                .replace(R.id.userPgFragmentViewer, storyFragment)
                                .commit();
                        break;
                    case FOLLOWING_FRAGMENT_POSITION:
                        FollowingFragment followingFragment = FollowingFragment.newInstance(clickedUser, authToken);
                        fm.beginTransaction()
                                .replace(R.id.userPgFragmentViewer, followingFragment)
                                .commit();
                        break;
                    case FOLLOWER_FRAGMENT_POSITION:
                        FollowerFragment followerFragment = FollowerFragment.newInstance(clickedUser, authToken);
                        fm.beginTransaction()
                                .replace(R.id.userPgFragmentViewer, followerFragment)
                                .commit();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MainActivity.CURRENT_USER_KEY, mainUser);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void followNumberSuccessful(FollowNumberResponse followNumberResponse) {
        followeeCount.setText(String.valueOf(followNumberResponse.getFollowingNumber()));
        followerCount.setText(String.valueOf(followNumberResponse.getFollowerNumber()));
    }

    @Override
    public void followNumberUnsuccessful(FollowNumberResponse followNumberResponse) {
        Toast.makeText(UserActivity.this, "Failed to get follow numbers. " + followNumberResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleFollowNumException(Exception ex) {
        Log.e(LOG_TAG_FOLLOW_NUM, ex.getMessage(), ex);

        if(ex instanceof TweeterRemoteException) {
            TweeterRemoteException remoteException = (TweeterRemoteException) ex;
            Log.e(LOG_TAG_FOLLOW_NUM, "Remote Exception Type: " + remoteException.getRemoteExceptionType());

            Log.e(LOG_TAG_FOLLOW_NUM, "Remote Stack Trace:");
            if(remoteException.getRemoteStackTrace() != null) {
                for(String stackTraceLine : remoteException.getRemoteStackTrace()) {
                    Log.e(LOG_TAG_FOLLOW_NUM, "\t\t" + stackTraceLine);
                }
            }
        }

        Toast.makeText(UserActivity.this, "Failed to get follow numbers because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void followSuccessful(FollowResponse followResponse) {
        int followNum = Integer.parseInt(followerCount.getText().toString());
        followerCount.setText(String.valueOf(followNum+1));
    }

    @Override
    public void followUnsuccessful(FollowResponse followResponse) {
        Toast.makeText(UserActivity.this, "Failed to follow. " + followResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleFollowException(Exception ex) {
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

        Toast.makeText(UserActivity.this, "Failed to follow because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void unfollowSuccessful(UnfollowResponse unfollowResponse) {
        int followNum = Integer.parseInt(followerCount.getText().toString());
        followerCount.setText(String.valueOf(followNum-1));
    }

    @Override
    public void unfollowUnsuccessful(UnfollowResponse unfollowResponse) {
        Toast.makeText(UserActivity.this, "Failed to unfollow. " + unfollowResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleUnfollowException(Exception ex) {
        Log.e(LOG_TAG_UNFOLLOW, ex.getMessage(), ex);

        if(ex instanceof TweeterRemoteException) {
            TweeterRemoteException remoteException = (TweeterRemoteException) ex;
            Log.e(LOG_TAG_UNFOLLOW, "Remote Exception Type: " + remoteException.getRemoteExceptionType());

            Log.e(LOG_TAG_UNFOLLOW, "Remote Stack Trace:");
            if(remoteException.getRemoteStackTrace() != null) {
                for(String stackTraceLine : remoteException.getRemoteStackTrace()) {
                    Log.e(LOG_TAG_UNFOLLOW, "\t\t" + stackTraceLine);
                }
            }
        }

        Toast.makeText(UserActivity.this, "Failed to unfollow because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

}
