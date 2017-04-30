package com.project.ruddle.handlers;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.project.ruddle.HomeActivity;
import com.project.ruddle.R;
import com.project.ruddle.adapters.PostAdapter;
import com.project.ruddle.post.NewPostActivity;

public class NavigationHandler {
    ///?maybe just add this class to HomeActivity

    public static void listen(final Context currentContext, BottomNavigationView navigation) {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        ((HomeActivity)currentContext).loadAllPostsList();
//                        currentContext.startActivity(new Intent(currentContext, HomeActivity.class));
                        return true;
                    case R.id.navigation_profile:
//                        currentContext.startActivity(new Intent(currentContext, ProfileActivity.class));
                        return true;
                    case R.id.navigation_new_post:
                        currentContext.startActivity(new Intent(currentContext, NewPostActivity.class));
                        return true;
                    case R.id.navigation_in_progress:
                        HomeActivity hm = (HomeActivity)currentContext;
                        hm.recyclerView = (RecyclerView) hm.findViewById(R.id.home_posts_list);
                        hm.recyclerView.setHasFixedSize(true);

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(hm);
                        hm.recyclerView.setLayoutManager(mLayoutManager);

                        refreshContent(hm);
                        return true;
                }
                return false;
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static void refreshContent(HomeActivity hm){
//        new hm.GetPostsTask().execute(); //later for inProgress just get another all(inProgress)PostsTask

        PostAdapter mAdapter = new PostAdapter(hm.postDataset);
        hm.recyclerView.setAdapter(mAdapter);
    }

}
