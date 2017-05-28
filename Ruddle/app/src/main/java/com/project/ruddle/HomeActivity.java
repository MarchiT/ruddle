package com.project.ruddle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click twice to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }


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

        navigationToggle();
    }


    private void navigationToggle() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                item -> {
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
                };

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navigation_home);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(getApplicationContext(),"Settings selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.search:
                Toast.makeText(getApplicationContext(),"Search icon selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.sort:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
