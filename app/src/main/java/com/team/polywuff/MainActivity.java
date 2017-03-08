package com.team.polywuff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView viewEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // our Main Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // we can fetch users details by making a firebase user object
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();

        /*if(firebaseAuth.getCurrentUser() == null || AccessToken.getCurrentAccessToken() == null){
            finish();
            startActivity(new Intent(this,FacebookLogin.class));
        }
        // if

        if(AccessToken.getCurrentAccessToken() == null){
            backToLogin();
        }*/
        if (user == null){
            backToLogin();
        }

        /*viewEmail = (TextView) findViewById(R.id.textView);
        viewEmail.setText("Welcome " + user.getEmail());
*/

/*
errors
        // if token is nothing then go back to login screen!
        if(AccessToken.getCurrentAccessToken() == null || user == null){
            backToLogin();
            //startActivity(new Intent(this,FacebookLogin.class));
        }
*/


    }

    // Logout button TEMP!
    public void getMeOut(View v){
        // Facebook already has a logout method
        // which can be called anywhere in our application!
        LoginManager.getInstance().logOut();
        // logout firebase
        FirebaseAuth.getInstance().signOut();
        backToLogin();
    }

    private void backToLogin() {
        Intent intent = new Intent(this,FacebookLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
