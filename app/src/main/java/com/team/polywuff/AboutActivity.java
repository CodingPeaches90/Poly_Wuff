/*
* @author: Emma English, x15575767
 */

package com.team.polywuff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    //Textview box that will display block of informational text
    TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Textview constructor
        details = (TextView) findViewById(R.id.about);

        //Setting textview to html of block of text in @string.xml
        details.setText(Html.fromHtml(getString(R.string.about_text)));

    }
}
