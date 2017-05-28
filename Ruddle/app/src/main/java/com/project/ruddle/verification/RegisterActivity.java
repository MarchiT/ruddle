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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.ruddle.constants.References.SERVER_URL;
import static com.project.ruddle.handlers.RequestHandler.sendPostStatus;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    
    public void register(View view) {
        String name;
        String password;
        String email;

            View focusView = null;

            EditText editUsername = (EditText) findViewById(R.id.register_name);
            EditText editPassword = (EditText) findViewById(R.id.register_password);
            EditText editEmail = (EditText) findViewById(R.id.register_email);

            name = editUsername.getText().toString();
            password = editPassword.getText().toString();
            email = editEmail.getText().toString();

            //return back and write message
            if (!isDataValid(name, password, email)) {
                Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            } else {
                UserRegisterTask b = new UserRegisterTask();
                b.execute(name, email, password);
            }
    }

    private boolean isDataValid(String name, String password, String email) {
        return isNameValid(name) && isPassValid(password) && isEmailValid(email);
    }

    private boolean isNameValid(String name){
        return (name.length() > 3);
    }

    private boolean isPassValid(String password){
        return (password.length() > 4);
    }

    private boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile("^[^@\\n\\s]+@[^@\\n]+$");
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
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
            return sendPostStatus(SERVER_URL + "register", urlParams);
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
