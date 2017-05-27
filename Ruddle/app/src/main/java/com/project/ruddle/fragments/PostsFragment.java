package com.project.ruddle.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ruddle.R;
import com.project.ruddle.adapters.PostAdapter;

public class PostsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.list_posts_layout, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.home_posts_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        PostAdapter mAdapter = new PostAdapter(getArguments().getString("posts"), getActivity());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
