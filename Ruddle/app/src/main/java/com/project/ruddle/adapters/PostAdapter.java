package com.project.ruddle.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.ruddle.R;

import org.json.JSONArray;
import org.json.JSONException;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private JSONArray posts;

    public PostAdapter (String posts) {
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
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
//          final String name = posts.get(position).get("title");
            holder.txtHeader.setText(posts.getJSONObject(position).getString("title"));
            holder.txtFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
    //                Intent post = new Intent(this, PostActivity.class);
    //                post.putExtra("Title", )
    //                startActivity(post);
                }
            });
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
