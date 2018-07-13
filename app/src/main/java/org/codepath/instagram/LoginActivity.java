package org.codepath.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput= findViewById(R.id.etUsername);
        passwordInput= findViewById(R.id.etPassword);
        loginBtn=findViewById(R.id.login_button);
        signupBtn=findViewById(R.id.signup_button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username=usernameInput.getText().toString();
                final String password= passwordInput.getText().toString();
                login(username, password);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username=usernameInput.getText().toString();
                final String password= passwordInput.getText().toString();
                SignUp(username, password);

            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.d("LoginActivity", "Already Logged in");
            final Intent intent= new Intent (LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void SignUp(String username, String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.put("Bio", "Hewwo");
        // Invoke signUpInBackground

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("LoginActivity", "SignUp Successfull");
                    final Intent intent= new Intent (LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.d("LoginActivity", "SignUp failure");
                    e.printStackTrace();
                }
            }
        });

    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e==null){
                    currentUser=user;
                    Log.d("LoginActivity", "Login Successfull");
                    final Intent intent= new Intent (LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.d("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }



}
