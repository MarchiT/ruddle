package com.project.ruddle.verification;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.ruddle.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.project.ruddle.handlers.RequestHandler.sendPostStatus;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    //TODO add verification like in login
    public void register(View view) {
        EditText editUsername = (EditText) findViewById(R.id.register_name);
        EditText editPassword = (EditText) findViewById(R.id.register_password);
        EditText editEmail = (EditText) findViewById(R.id.register_email);

        String name = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String email = editEmail.getText().toString();

        UserRegisterTask b = new UserRegisterTask();
        b.execute(name, email, password);
    }

    private class UserRegisterTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            String password = params[2];

            JSONObject urlParams = new JSONObject();
            try {
                urlParams.put("name", name);
                urlParams.put("email", email);
                urlParams.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return sendPostStatus("http://10.0.2.2:8000/register", urlParams);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                Toast.makeText(RegisterActivity.this, "Data saved successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
            }
        }
    }

}
