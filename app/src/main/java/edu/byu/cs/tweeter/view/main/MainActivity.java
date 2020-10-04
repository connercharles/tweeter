package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
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
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.view.StartUpActivity;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowingTask;
import edu.byu.cs.tweeter.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

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
                Popup popup = Popup.newInstance();
                popup.show(fm, "Popup_Fragment");
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText("Following: " + "-42");

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText("Followers: " + "-42");

        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, StartUpActivity.class);
                startActivity(intent);
            }
        });


//        logout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){ //******************************************** TODO how should we do logging out???
//                class LogoutClass implements LogoutPresenter.View, LogoutTask.Observer {
//
//                    @Override
//                    public void logoutSuccessful(LogoutResponse logoutResponse) {
//                        Intent intent = new Intent(MainActivity.this, StartUpActivity.class);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
//                        Toast.makeText(MainActivity.this, "Failed to logout. " + logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void handleException(Exception ex) {
//                        Log.e("Logout", ex.getMessage(), ex);
//                        Toast.makeText(MainActivity.this, "Failed to logout because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//
//                LogoutRequest logoutRequest = new LogoutRequest(authToken);
//                LogoutTask loginTask = new LogoutTask(presenter, LogoutClass.class);
//                logoutTask.execute(logoutRequest);
//
//
//                new LogoutClass();
//            }
//        });
    }

    private void getFollowNumbers(){ //***************************** TODO how do I do this????
//        GetFollowingTask getFollowingTask = new GetFollowingTask(presenter, this);
//        FollowingRequest request = new FollowingRequest(user, PAGE_SIZE, lastFollowee);
//        getFollowingTask.execute(request);
    }
}