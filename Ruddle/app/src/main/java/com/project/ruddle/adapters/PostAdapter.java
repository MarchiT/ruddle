package com.project.ruddle.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.project.ruddle.R;
import com.project.ruddle.post.PostActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;

    private JSONArray posts;


    public PostAdapter (String posts, Context context) {
        this.context = context;
        try {
            this.posts = new JSONArray(posts);
        } catch (JSONException e) {
            Log.e("PostAdapter", "forming JSONArray failure");
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtHeader;
        private TextView txtFooter;

        private RadioButton inProgressButton;

        private ViewHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);
            inProgressButton = (RadioButton) itemView.findViewById(R.id.radioButton);
        }
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        view.setOnClickListener((v -> {
            int itemPosition = ((RecyclerView)parent).getChildLayoutPosition(view);
            Intent intent = new Intent(new Intent(context, PostActivity.class));
            try {
                intent.putExtra("post", posts.getJSONObject(itemPosition).toString());
            } catch (JSONException e) {
                Log.e("PostAdapter", "onCreateViewHolder");
            }
            context.startActivity(intent);
        }));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtHeader.setText(posts.getJSONObject(position).getString("title"));
            holder.txtFooter.setText(posts.getJSONObject(position).getString("body"));

            String tag = posts.getJSONObject(position).getString("tag");

            if (tag.length() == 0 || tag.equals("null")) {
                holder.inProgressButton.setVisibility(View.VISIBLE);
                holder.inProgressButton.setButtonDrawable(R.drawable.ic_post_list_no_relation);
            } else if (tag.equals("inprogress")) {
                holder.inProgressButton.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            Log.e("PostAdapter", "JSONArray position wrong");
        }
    }

    @Override
    public int getItemCount() {
        return posts.length();
    }

}
