package com.project.ruddle.post;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.ruddle.HomeActivity;
import com.project.ruddle.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.project.ruddle.R.id.answer;
import static com.project.ruddle.R.id.body;
import static com.project.ruddle.handlers.RequestHandler.sendPostStatus;

public class NewPostActivity extends AppCompatActivity {
    //TODO add validation for here too
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

        NewPostActivity.PostRegisterTask b = new NewPostActivity.PostRegisterTask();
        b.execute(title, body, answer);
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
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            return sendPostStatus("http://10.0.2.2:8000/posts", urlParams);
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