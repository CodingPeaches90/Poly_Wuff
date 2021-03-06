/*
* author: Dziugas Grusaukas, x15304641
 */

package com.team.polywuff;

        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

//@Reference http://www.viralandroid.com/2015/11/android-calendarview-example.html


public class Calendar extends AppCompatActivity {

    CalendarView calendarView;
    TextView dateDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

// To show the Calendar we used the CalendarView widget supported by Android

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        dateDisplay = (TextView) findViewById(R.id.date_display);
        dateDisplay.setText("Date: ");


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                i1 = i1+1;
                dateDisplay.setText("Date: " + i2 + " / " + i1 + " / " + i);
                // This displays day/month/year

            }
        });
    }
}