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
import android.widget.RadioButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        final RadioButton inProgressButton = (RadioButton) findViewById(R.id.postAcivityRadioButton);

        String status = null;
        try {
            post = new JSONObject(getIntent().getStringExtra("post"));
            status = post.getString("tag");
        } catch (JSONException e) {
            Log.e("PostActivity", "onCreate");
        }
        Log.i("This post contains", post.toString());

        TextView usernameView = (TextView) findViewById(R.id.username);

        TextView titleView = (TextView) findViewById(R.id.title);
        TextView bodyView = (TextView) findViewById(R.id.body);

        try {
            titleView.setText(post.getString("title"));
            bodyView.setText(post.getString("body"));
            usernameView.setText(post.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status == null || (status.equals("created") || status.equals("solved"))) {
            EditText answer = (EditText) findViewById(R.id.answer);
            Button submit = (Button) findViewById(R.id.submit);

            answer.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        } else {
            inProgressButton.setVisibility(View.VISIBLE);
            inProgressButton.setChecked(status.equals("inprogress"));
        }

        final boolean pinButtonChecked = status != null && status.equals("inprogress");
        inProgressButton.setOnClickListener(v -> {

            if (!pinButtonChecked) {
                new TagRegisterTask().execute("inprogress");
            } else {
                new TagRegisterTask().execute("nothing");
            }
        });
    }

    public void submitAnswer(View view) {
        EditText answerBox = (EditText)findViewById(R.id.answer);
        String currentAnswer = answerBox.getText().toString();
        String postAnswer;

        try {
            postAnswer = post.getString("answer");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (currentAnswer.toLowerCase().equals(postAnswer.toLowerCase())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            new PostActivity.TagRegisterTask().execute("solved");
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
                urlParams.put("tag", params[0]);
            } catch (JSONException e) {
                Log.e("TagRegisterTask", e.getMessage());
            }
            Log.i("Posting to server", urlParams.toString());
            return sendPostStatus(SERVER_URL + "userposts", urlParams);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) startActivity(new Intent(PostActivity.this, HomeActivity.class));
            else Log.e("UserPosts", "PostActivity POST failed");
        }
    }
}
