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
import org.json.JSONObject;

import java.util.ArrayList;

import static com.project.ruddle.constants.References.SERVER_URL;
import static com.project.ruddle.handlers.RequestHandler.sendGet;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<String> postDataset = new ArrayList<>();

    private JSONObject currentJson = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
        String name = settings.getString("user_id", "user");

        TextView nameTV = (TextView) findViewById(R.id.home_name);
        nameTV.setText("Welcome, " + name);


        getAllPosts();
        loadPostsFragment();


        loadAllPostsList();

    }

    private class GetPostsTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return sendGet(SERVER_URL + "posts");
//            return sendGet(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                addPosts(new JSONArray(s));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON: ", "HomeActivity GetPostsTask adding failed");
            }
        }
    }

    private class GetPostTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return sendGet(SERVER_URL + "post/" + params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
//                JSONObject post = new JSONObject(s);
                currentJson = new JSONObject(s);
                Log.e("currentJson", currentJson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON: ", "HomeActivity GetPostTask adding failed");
            }
        }
    }

    private class GetSpecificPostsTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return sendGet(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                addPosts(new JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON: ", "HomeActivity GetSpecificPosts adding failed");
            }
        }
    }

    private void addPosts(JSONArray posts) throws JSONException {
        for (int i = 0; i < posts.length(); i++) {
            postDataset.add(posts.getJSONObject(i).get("title").toString());
        }
    }

    private void addPosts(JSONObject posts) throws JSONException {
        for (Integer i = 0; i < posts.length(); i++) {
            JSONObject indexPost = (JSONObject) posts.get(i.toString());
            //make GET to /post/{id} and take the title at this stage
            new GetPostTask().execute(indexPost.get("post_id").toString());
            Log.e("currentJson", currentJson.get("title").toString());

//            postDataset.add(currentJson.get("title").toString());
            postDataset.add(indexPost.get("post_id").toString());
        }
    }


    private void loadAllPostsList() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                Log.e("Home", "entered");
                                getAllPosts();
                                //1. it needs to be pressed twice to display stuff, why?
                                //changes apply only after the second click ??
                                //2. if clicked once more (doesn't refresh) but keeps adding the new posts below the old ones
                                loadPostsFragment();
                                return true;
                            case R.id.navigation_profile:
                                Log.e("Profile", "entered");
                                return true;
                            case R.id.navigation_new_post:
                                Log.e("NewPost", "entered");
                                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                                return true;
                            case R.id.navigation_in_progress:
                                Log.e("InProgress", "entered");
                                getCreatedPosts();
                                loadAllPostsList();
                                return true;
                        }
                        return false;
                    }
                };

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navigation_home);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        loadPostsFragment();
    }

    private void loadPostsFragment() {
//        getCreatedPosts();

        Fragment fragment = new PostsFragment();
        Bundle args = new Bundle();

        args.putStringArrayList("titles", postDataset);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.list_posts_frame, fragment).commit();
        postDataset = new ArrayList<>();
    }

    private void getAllPosts() {
        new GetPostsTask().execute();
    }
    private void getCreatedPosts() {
        SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
        new GetSpecificPostsTask().execute(SERVER_URL + "posts/created/" + settings.getString("user_id", "0"));
    }


    public void logOut(View view) {
        SharedPreferences settings = getSharedPreferences(References.USER, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("hasLoggedIn", false);

        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
    }

}
