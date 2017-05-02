package com.project.ruddle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.ruddle.constants.References;
import com.project.ruddle.handlers.PostsFragment;
import com.project.ruddle.post.NewPostActivity;
import com.project.ruddle.verification.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.project.ruddle.constants.References.SERVER_URL;
import static com.project.ruddle.handlers.RequestHandler.sendGet;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<String> postDataset = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
        String name = settings.getString("name", "user");

        TextView nameTV = (TextView) findViewById(R.id.home_name);
        nameTV.setText("Welcome, " + name);


        getAllPosts();

        NavigationToggle();
    }


    private class GetPostsTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return sendGet(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                addPosts(new JSONArray(s));
                loadPostsFragment();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON: ", "HomeActivity GetPostsTask adding failed");
            }
        }
    }

    private void addPosts(JSONArray posts) throws JSONException {
        for (int i = 0; i < posts.length(); i++) {
            postDataset.add(posts.getJSONObject(i).get("title").toString());
        }
    }

    private void loadPostsFragment() {
        Fragment fragment = new PostsFragment();
        Bundle args = new Bundle();

        args.putStringArrayList("titles", postDataset);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.list_posts_frame, fragment).commit();
        postDataset = new ArrayList<>();
    }


    private void getAllPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts");
    }
    private void getCreatedPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts/created/" + getUserId());
    }
    private void getInProgressPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts/inprogress/" + getUserId());
    }
    private void getSolvedPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts/solved/" + getUserId());
    }

    private String getUserId() {
        SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
        return settings.getString("user_id", "0");
    }


    private void NavigationToggle() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                getAllPosts();
                                return true;
                            case R.id.navigation_profile:
                                //TODO implement ProfileActivity
                                return true;
                            case R.id.navigation_new_post:
                                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                                return true;
                            case R.id.navigation_in_progress:
                                getInProgressPosts();
                                return true;
                        }
                        return false;
                    }
                };

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navigation_home);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void logOut(View view) {
        SharedPreferences settings = getSharedPreferences(References.USER, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("hasLoggedIn", false);

        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
    }

}
