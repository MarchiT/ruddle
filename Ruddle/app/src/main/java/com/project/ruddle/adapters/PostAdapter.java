package com.project.ruddle.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        private ViewHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);
        }
    }

//    private void add(int position, String item) {
//        posts.get("titles").add(position, item);
//        notifyItemInserted(position);
//    }
//
//    private void remove(String item) {
//        int position = posts.get("titles").indexOf(item);
//        posts.get("titles").remove(position);
//        notifyItemRemoved(position);
//    }

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
        } catch (JSONException e) {
            Log.e("PostAdapter", "JSONArray position wrong");
        }
    }

    @Override
    public int getItemCount() {
        return posts.length();
    }

}
