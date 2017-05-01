package com.project.ruddle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.ruddle.adapters.PostAdapter;
import com.project.ruddle.constants.References;
import com.project.ruddle.post.NewPostActivity;
import com.project.ruddle.verification.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.project.ruddle.handlers.RequestHandler.sendGet;

public class HomeActivity extends AppCompatActivity {

    public List<String> postDataset = new ArrayList<>();

    public RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
        String name = settings.getString("name", "user");

        TextView nameTV = (TextView) findViewById(R.id.home_name);
        nameTV.setText("Welcome, " + name);

        loadAllPostsList();

    }


    private class GetPostsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return sendGet("http://10.0.2.2:8000/posts");
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                addPosts(new JSONArray(s));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON: ", "HomeActivity adding failed");
            }
        }
    }

    private void addPosts(JSONArray posts) throws JSONException {
        for (int i = 0; i < posts.length(); i++) {
            postDataset.add(posts.getJSONObject(i).get("title").toString());
        }
    }

    private void refreshContent(RecyclerView recyclerView){
        new GetPostsTask().execute();

        PostAdapter mAdapter = new PostAdapter(postDataset);
        recyclerView.setAdapter(mAdapter);
    }

    public void loadAllPostsList() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                return true;
                            case R.id.navigation_profile:
                                return true;
                            case R.id.navigation_new_post:
                                startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                                return true;
                            case R.id.navigation_in_progress:
                                return true;
                        }
                        return false;
                    }
                };

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navigation_home);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = (RecyclerView) findViewById(R.id.home_posts_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        refreshContent(recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent(recyclerView);
                //TODO implement proper refresh
                TextView nameTV = (TextView) findViewById(R.id.home_name); //it refreshes if new Activity starts
                nameTV.setText("this is bullshit");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void logOut(View view) {
        SharedPreferences settings = getSharedPreferences(References.USER, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("hasLoggedIn", false);

        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
    }
}
