/*
 * @author Christopher Kambayi, x15513473
 * @author Emma English, x15575767
 *
 */
package com.team.polywuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SettingsActivity extends AppCompatActivity {

    //String array declaration an constructor
    private String [] items = {"General", "Change password", "Delete account", "About"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Arrayadapter of items to be displayed by list view
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        //List view declaration and construction
        ListView listView = (ListView) findViewById(R.id.lvMenu);
        //Arrayadapter element loaded to list view
        listView.setAdapter(itemsAdapter);

        //lst view on click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // first item clicked
                if(position == 0)
                {
                    openGeneralActivity();
                }
                // second item clicked
                else if(position == 1)
                {
                    openPasswordActivity();
                }
                // third item clicked
                else if(position == 2)
                {
                    openDeleteAccountActivity();
                }
                // fourth item clicked
                else if(position == 3)
                {
                    openAboutActivity();
                }
            }
        });

    }

    //method to open General activity
    public void openGeneralActivity(){
        Intent intent;
        intent = new Intent(this,GeneralActivity.class);
        startActivity(intent);
    }

    //method to open Password change activity
    public void openPasswordActivity(){
        Intent intent;
        intent = new Intent(this,PasswordActivity.class);
        startActivity(intent);
    }

    //method to open Delete account activity
    public void openDeleteAccountActivity(){
        Intent intent;
        intent = new Intent(this,DeleteAccountActivity.class);
        startActivity(intent);
    }

    //method to open About activity
    public void openAboutActivity(){
        Intent intent;
        intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
    }
}
