package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.main.following.FollowerFragment;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity {
    public static final String CLICKED_USER = "ClickedUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String FOLLOW_STRING = "Follow";
    public static final String FOLLOWING_STRING = "Following";

    private static final int STORY_FRAGMENT_POSITION = 0;
    private static final int FOLLOWING_FRAGMENT_POSITION = 1;
    private static final int FOLLOWER_FRAGMENT_POSITION = 2;


    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getSerializableExtra(CLICKED_USER);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        TextView userName = findViewById(R.id.userPgName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userPgAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userPgImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        TextView followeeCount = findViewById(R.id.userPgFolloweeCount);
        followeeCount.setText("Following: " + "-42"); //****************************************8 TODO change based on request?

        TextView followerCount = findViewById(R.id.userPgFollowerCount);
        followerCount.setText("Followers: " + "-42");

        Button followBtn = findViewById(R.id.userPgFollowButton);
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followBtn.getText().equals(FOLLOW_STRING)){
                    followBtn.setText(FOLLOWING_STRING);
                    followBtn.setBackgroundColor(Color.WHITE);
                    followBtn.setTextColor(Color.BLACK);
                    
                    //************************************************************************ TODO send follow request and such
                } else{
                    followBtn.setText(FOLLOW_STRING);
                    followBtn.setBackgroundColor(Color.parseColor("#D81B60"));
                    followBtn.setTextColor(Color.WHITE);
                }
            }
        });

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
        StoryFragment storyFragment = StoryFragment.newInstance(user, authToken);
        fm.beginTransaction()
                .add(R.id.userPgFragmentViewer, storyFragment)
                .commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();

                switch (tab.getPosition()){
                    case STORY_FRAGMENT_POSITION:
                        StoryFragment storyFragment = StoryFragment.newInstance(user, authToken);
                        fm.beginTransaction()
                                .replace(R.id.userPgFragmentViewer, storyFragment)
                                .commit();
                        break;
                    case FOLLOWING_FRAGMENT_POSITION:
                        FollowingFragment followingFragment = FollowingFragment.newInstance(user, authToken);
                        fm.beginTransaction()
                                .replace(R.id.userPgFragmentViewer, followingFragment)
                                .commit();
                        break;
                    case FOLLOWER_FRAGMENT_POSITION:
                        FollowerFragment followerFragment = FollowerFragment.newInstance(user, authToken);
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
            startActivity(intent);
        }
        return true;
    }

}
