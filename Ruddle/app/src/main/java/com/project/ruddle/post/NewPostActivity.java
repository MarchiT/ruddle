package com.project.ruddle.post;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.ruddle.HomeActivity;
import com.project.ruddle.R;
import com.project.ruddle.constants.References;

import org.json.JSONException;
import org.json.JSONObject;

import static com.project.ruddle.R.id.answer;
import static com.project.ruddle.R.id.body;
import static com.project.ruddle.constants.References.SERVER_URL;
import static com.project.ruddle.handlers.RequestHandler.sendPostStatus;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    public void submitPost(View view) {
        EditText editTitle = (EditText) findViewById(R.id.title);
        EditText editBody = (EditText) findViewById(body);
        EditText editAnswer = (EditText) findViewById(answer);

        String title = editTitle.getText().toString();
        String body = editBody.getText().toString();
        String answer = editAnswer.getText().toString();

        if (isPostValid(title, body, answer)) {
            NewPostActivity.PostRegisterTask b = new NewPostActivity.PostRegisterTask();
            b.execute(title, body, answer);
        }
    }

    private boolean isPostValid(String title, String body, String answer) {
        return isNull(title, "Title") && isNull(body, "Body") && isNull(answer, "Answer");
    }

    private boolean isNull(String part, String part_name){
        boolean valid = (part.length() > 0);
        if(!valid){
            Toast.makeText(this, part_name + " is empty", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private class PostRegisterTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String title = params[0];
            String body = params[1];
            String answer = params[2];

            JSONObject urlParams = new JSONObject();
            try {
                urlParams.put("title", title);
                urlParams.put("body", body);
                urlParams.put("answer", answer);

                SharedPreferences settings = getSharedPreferences(References.USER, MODE_PRIVATE);
                String id = settings.getString("user_id", "0");

                urlParams.put("id", id);
                urlParams.put("tag", "created");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            return sendPostStatus(SERVER_URL + "posts", urlParams);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                Toast.makeText(NewPostActivity.this, "Post uploaded.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(NewPostActivity.this, HomeActivity.class));
            } else {
                Toast.makeText(NewPostActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
