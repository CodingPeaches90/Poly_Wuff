package com.team.polywuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

//@Reference https://www.youtube.com/watch?v=6qo_Opqjhew

public class Home_Activity extends AppCompatActivity {
    public ImageButton assist;
    public ImageButton exercise;
    public ImageButton socialise;

//sends to the socialise about page
    public void sendSocialise(){
        socialise = (ImageButton)findViewById(R.id.socialise);
        socialise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendSocialise = new Intent(Home_Activity.this, Socialise.class);
                startActivity(sendSocialise);

            }
        });
    }

    //sends to the exercise about page
    public void sendExercise(){
        exercise = (ImageButton)findViewById(R.id.exercise);
        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Intent sendExercise = new Intent(Home_Activity.this, Exercise.class);

                startActivity(sendExercise);
            }
        });
    }

    //sends to the assist page
    public void sendAssist(){
        assist = (ImageButton)findViewById(R.id.assist);
        assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent sendAssist = new Intent(Home_Activity.this, Assist.class);

                startActivity(sendAssist);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sendAssist();
        sendExercise();
        sendSocialise();


    }

}
