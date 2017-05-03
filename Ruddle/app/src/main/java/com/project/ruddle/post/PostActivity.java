package com.project.ruddle.post;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.ruddle.R;

import org.json.JSONException;
import org.json.JSONObject;


public class PostActivity extends AppCompatActivity {

    private JSONObject post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        try {
            post = new JSONObject(getIntent().getStringExtra("post"));
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
            //TODO Post request to server
        } else {
            Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
