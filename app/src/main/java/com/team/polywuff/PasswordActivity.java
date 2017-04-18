package com.team.polywuff;
/*
* @author: Jordan May, x15515673
* */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PasswordActivity extends AppCompatActivity {

    //Declaring user, button, edittext and textview
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button changePassword;
    private EditText inputPassword;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Contructor of textview, editview and button
        message = (TextView) findViewById(R.id.instructions) ;
        inputPassword = (EditText) findViewById(R.id.newPassword);
        changePassword = (Button) findViewById(R.id.confirmPassword);

        //Button click listener
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call method to change user password taking edittext as input
                changePassword(inputPassword);
            }
        });
    }

    //method to change user password
    public void changePassword(EditText password){

        String newPassword = password.toString();

        //button pressed and nothing enterd. Push a warning toast
        if(newPassword.equals(""))
        {
            Toast.makeText(PasswordActivity.this,"Please enter a valid password",Toast.LENGTH_SHORT).show();
        }
        else //Valid password entered. New password is updated with firebase method and back method called
        {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(PasswordActivity.this,"Password changed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            back();
        }

    }

    //method that returns the user to the Settings activity after successful password change
    private void back() {
        Intent intent = new Intent(this,SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
