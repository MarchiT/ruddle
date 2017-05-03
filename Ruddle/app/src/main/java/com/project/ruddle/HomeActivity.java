package com.project.ruddle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.project.ruddle.constants.References;
import com.project.ruddle.fragments.ProfileFragment;
import com.project.ruddle.handlers.ListsHandler;
import com.project.ruddle.handlers.PostLists;
import com.project.ruddle.post.NewPostActivity;
import com.project.ruddle.verification.LoginActivity;

import org.json.JSONArray;

public class HomeActivity extends AppCompatActivity {

    public JSONArray posts;

    private final PostLists listsHandler = new ListsHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(References.USER, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(!hasLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        listsHandler.getAllPosts();

        NavigationToggle();
    }


    private void NavigationToggle() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                listsHandler.getAllPosts();
                                return true;
                            case R.id.navigation_profile:
                                Fragment profileFragment = new ProfileFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.list_posts_frame, profileFragment).commit();
                                return true;
                            case R.id.navigation_new_post:
                                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                                return true;
                            case R.id.navigation_in_progress:
                                listsHandler.getInProgressPosts();
                                return true;
                        }
                        return false;
                    }
                };

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navigation_home);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
