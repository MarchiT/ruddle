package com.project.ruddle.post;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.ruddle.HomeActivity;
import com.project.ruddle.R;
import com.project.ruddle.constants.References;

import org.json.JSONException;
import org.json.JSONObject;

import static com.project.ruddle.constants.References.SERVER_URL;
import static com.project.ruddle.handlers.RequestHandler.sendPostStatus;


public class PostActivity extends AppCompatActivity {

    private JSONObject post;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        try {
            post = new JSONObject(getIntent().getStringExtra("post"));
            status = getIntent().getStringExtra("status");
            //for now
            if (status == null) {
                status = "inprogress";
            }
        } catch (JSONException e) {
            Log.e("PostActivity", "onCreate");
        }

//        TextView username = (TextView) findViewById(R.id.username);

        TextView titleView = (TextView) findViewById(R.id.title);
        TextView bodyView = (TextView) findViewById(R.id.body);

        try {
            titleView.setText(post.getString("title"));
            bodyView.setText(post.getString("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equals("created")) {
            EditText answer = (EditText) findViewById(R.id.answer);
            Button submit = (Button) findViewById(R.id.submit);
            answer.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }
    }

    public void submitAnswer(View view) {
        EditText answerBox = (EditText)findViewById(R.id.answer);
        String currentAnswer = answerBox.getText().toString();
        String postAnswer = null;

        try {
            postAnswer = post.getString("answer");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (currentAnswer.toLowerCase().equals(postAnswer.toLowerCase())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            new PostActivity.TagRegisterTask().execute();
        } else {
            Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }


    private class TagRegisterTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject urlParams = new JSONObject();
            try {
                SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
                String id = settings.getString("user_id", "0");

                urlParams.put("user_id", id);
                urlParams.put("post_id", post.getString("id"));
                urlParams.put("tag", "solved");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sendPostStatus(SERVER_URL + "userposts", urlParams);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) startActivity(new Intent(PostActivity.this, HomeActivity.class));
            else Log.e("UserPosts", "PostActivity POST failed");
        }
    }
}
