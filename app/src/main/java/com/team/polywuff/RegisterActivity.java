package com.team.polywuff;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {
    // in this class (FacebookLogin activity - when user pressess link bring them here
    // we use firebases standard login
    // take password and email from the textfields and store
    private FirebaseAuth firebaseAUTH;
    private FirebaseAuth.AuthStateListener mAuthL;


    private Button registerBTN;
    private EditText enteredEmail;
    private EditText enteredPassword;
    private EditText confirmPassword;

    String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // FIREBASE
        firebaseAUTH = FirebaseAuth.getInstance();

        registerBTN = (Button) findViewById(R.id.registerBTN);
        registerBTN.setOnClickListener(this);

        enteredEmail = (EditText)findViewById(R.id.enteredEmail);
        enteredPassword = (EditText) findViewById(R.id.enteredPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        mAuthL = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //signed in!
                    Log.d(TAG,"Signed in");
                }
            }
        };


    }
    public void clearFields(){
        // store temp strings
        String tempEmail = enteredEmail.getText().toString();
        String tempPass = enteredPassword.getText().toString();
        // match password
        String tempMatch = confirmPassword.getText().toString();

        if(TextUtils.isEmpty(tempEmail)){
            Toast.makeText(RegisterActivity.this,"Please enter a email!",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(tempPass) || TextUtils.isEmpty(tempMatch)){
            Toast.makeText(RegisterActivity.this,"Please enter a valid password",Toast.LENGTH_SHORT).show();
        }else if(tempPass != tempMatch){
            Toast.makeText(RegisterActivity.this,"Passwords do not match!",Toast.LENGTH_SHORT).show();
            enteredPassword.getText().clear();
            confirmPassword.getText().clear();
        }else{
            registerTheUser();

        }

       /* if(tempEmail.isEmpty()){
            // if the email field is empty print out
            Toast.makeText(RegisterActivity.this,"Please enter a email!",Toast.LENGTH_SHORT).show();
        }else if(tempPass.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Please enter a valid password",Toast.LENGTH_SHORT).show();
        }else if(tempMatch.equals(tempPass)){
            Toast.makeText(RegisterActivity.this,"Passwords do not match!",Toast.LENGTH_SHORT).show();
            enteredPassword.getText().clear();
            confirmPassword.getText().clear();
        }else{
            registerTheUser();
        }*/
    }

    private void registerTheUser(){
        String email = enteredEmail.getText().toString();
        String password = enteredPassword.getText().toString();

        firebaseAUTH.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"REGISTERED!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else{
                    Toast.makeText(RegisterActivity.this,"Error!",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
    @Override
    public void onClick(View view){
        clearFields();

    }

    public void onStart(){
        super.onStart();
        firebaseAUTH.addAuthStateListener(mAuthL);
    }
    public void onStop(){
        super.onStop();
        firebaseAUTH.removeAuthStateListener(mAuthL);

    }
}
