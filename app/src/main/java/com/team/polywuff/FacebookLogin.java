/*
 * @author Jordan May, x15515673
 *
 */
package com.team.polywuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FacebookLogin extends AppCompatActivity implements View.OnClickListener {
    // NOTES
    /*
        use toast to display to user
        firebase object auth authenticate
        firebase listener


     */

    // normal sign in
    private EditText loginEmail;
    private EditText loginPassword;
    private Button LoginButtonNormal;



    private TextView not_have_account;

    private static final String TAG = "FacebookLogin";
    // We declare our firebase objects
    // Track sign in and sign out activity of user
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Login button fb
    private LoginButton login_button;

    // Call back manager
    // This manages our login and also access permission for the application
    private CallbackManager callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
// NORNMAL LOGIN ***************
        // initialise normal login in
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        LoginButtonNormal = (Button) findViewById(R.id.LoginButtonNormal);

        LoginButtonNormal.setOnClickListener(this);


//mainScreenOnSuccess() proceed to next activity


//*****END NORMAL
        // register link
        not_have_account = (TextView) findViewById(R.id.not_have_account);
        not_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // because method is public it wont allow instance i.e this.(class)
                Intent intent = new Intent(FacebookLogin.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        // We initialise our login button and also our call back manager
        // in the create method. When app loads do this

        callBack = CallbackManager.Factory.create();
        login_button = (LoginButton) findViewById(R.id.login_button);

        // we can set permissions for application
        // we want the users email and public profile
        login_button.setReadPermissions("email","public_profile");

        // now we register our call back which creates three additional
        // methods
        login_button.registerCallback(callBack, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // If the users login is successful then we want
                // them to go to the main activity of our application
                // we make a method here
                // When we use firebase we want to get the access token from fb
                faceBookAccessToken(loginResult.getAccessToken());

                mainScreenOnSuccess();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        //Initialise in our oncreate method
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // As long as the user is not null proceed to the main activity
                    mainScreenOnSuccess();
                }

            }
        };
    }
    //Authenticate with firebase using normal credentials
    // getting no sign in as i was not calling my method

    private void authenticateLogin(){
        // grab input from textfields and store in variables
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // when sign in complete do this
                if(task.isSuccessful()){
                    // only if task is successful call our main activity and proceed
                    //sendToOnclick = true;
                    Toast.makeText(getApplicationContext(),"Logging in",Toast.LENGTH_SHORT).show();
                    mainScreenOnSuccess();

                }else{
                    Toast.makeText(getApplicationContext(),"Unable to login",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    // onclick
    // error code : loop through activities
    public void onClickd(View view){
        LoginButtonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateLogin();

            }
        });
    }

    // Authentication with firebase



    private void faceBookAccessToken(AccessToken accessToken) {
        //Assign credential to the facebook access token
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        // authenticate with firebase


        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(),R.string.error,Toast.LENGTH_LONG).show();

                if(!task.isSuccessful()){
                    Log.w(TAG,"Sign in " + task.getException());
                }

            }
        });

    }

    private void mainScreenOnSuccess() {
        // Tell android to go to the main activity of our app
        Intent intent = new Intent(this,MainActivity.class);
        // We want to ensure that the user can not just go back and that our main activity is the only
        // activity running when successful login is complete
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    // This method is from the facebook developers website
    // It forwards the login results to the call back manager in our oncreate method!
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callBack.onActivityResult(requestCode,resultCode,data);
    }

    public void onClick(View view){
        if(view == LoginButtonNormal){
            authenticateLogin();
        }
    }



    @Override
    public void onStart(){
        // on start of the activity we add an auth listener
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        //When activity stops we remove the auth listener
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);


    }
}
