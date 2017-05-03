package com.project.ruddle.handlers;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.project.ruddle.HomeActivity;
import com.project.ruddle.R;
import com.project.ruddle.constants.References;
import com.project.ruddle.fragments.PostsFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.project.ruddle.constants.References.SERVER_URL;
import static com.project.ruddle.handlers.RequestHandler.sendGet;

public class ListsHandler implements PostLists {

    private HomeActivity ctx; //should it be bound to the HomeActivity class


    public ListsHandler(HomeActivity context) {
        this.ctx = context;
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
            ctx.postDataset.add(posts.getJSONObject(i).get("title").toString());
        }
    }

    private void loadPostsFragment() {
        Fragment postsFragment = new PostsFragment();
        Bundle args = new Bundle();

        args.putStringArrayList("titles", ctx.postDataset);
        postsFragment.setArguments(args);

        FragmentManager fragmentManager = ctx.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.list_posts_frame, postsFragment).commit();
        ctx.postDataset = new ArrayList<>();
    }


    @Override
    public void getAllPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts");
    }
    @Override
    public void getCreatedPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts/created/" + getUserId());
    }
    @Override
    public void getInProgressPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts/inprogress/" + getUserId());
    }
    @Override
    public void getSolvedPosts() {
        new GetPostsTask().execute(SERVER_URL + "posts/solved/" + getUserId());
    }

    private String getUserId() {
        SharedPreferences settings = ctx.getSharedPreferences(References.USER, MODE_PRIVATE);
        return settings.getString("user_id", "0");
    }
}
