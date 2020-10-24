package edu.byu.cs.tweeter.client.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;

/**
 * Contains the minimum UI required to allow the user to login with a hard-coded user. Most or all
 * of this should be replaced when the back-end is implemented.
 */
public class StartUpActivity extends AppCompatActivity {

    private static final String LOG_TAG = "StartUpActivity";

    private Toast loginInToast;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    static final int LOGIN_POSITION = 0;
    static final int REGISTER_POSITION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = new LoginFragment();
        fm.beginTransaction()
                .add(R.id.fragment_holder, loginFragment)
                .commit();


        TabLayout tabs = findViewById(R.id.login_tabs);
        TabLayout.Tab loginTab = tabs.newTab();
        loginTab.setText("Login");
        tabs.addTab(loginTab, LOGIN_POSITION, true);
        TabLayout.Tab registerTab = tabs.newTab();
        registerTab.setText("Register");
        tabs.addTab(registerTab, REGISTER_POSITION);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();

                switch (tab.getPosition()){
                    case LOGIN_POSITION:
                        loginFragment = new LoginFragment();
                        fm.beginTransaction()
                            .replace(R.id.fragment_holder, loginFragment)
                            .commit();
                        break;
                    case REGISTER_POSITION:
                        registerFragment = new RegisterFragment();
                        fm.beginTransaction()
                                .replace(R.id.fragment_holder, registerFragment)
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

}
