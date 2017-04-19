/*
 * @author: Christopher Kambayi, x15513473
 *
 */
package com.team.polywuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FilterActivity extends AppCompatActivity {

    //Declaring variables (Spinner, Button and String)
    private Spinner filter;
    private Button search;
    private String location;
    private String [] provinces = {"Leinster", "Munster", "Connacht", "Ulster"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Array adapter to be displayed by spinner
        ArrayAdapter<String> list = new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_spinner_item, provinces);

        //Constructor for spinner and button
        filter = (Spinner) findViewById(R.id.locationFilter);
        search = (Button) findViewById(R.id.searchBtn);

        //Setting filter item to display
        filter.setAdapter(list);

        //Item listener for spinner
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Sets location to corresponding province depending on what selected
                if(position == 0){
                    location = "Leinster";
                }
                else if(position == 1){
                    location = "Munster";
                }
                else if(position == 2){
                    location = "Connacht";
                }
                else if(position == 3){
                    location = "Ulster";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Button clicked listener
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user method is called
                runSearch(location);
            }
        });
    }

    //User method that opens a new activity and passes location
    public void runSearch(String location)
    {
        Intent intent = new Intent(this,SearchActivity.class);
        intent.putExtra("LOCATION", location);
        startActivity(intent);
    }
}
