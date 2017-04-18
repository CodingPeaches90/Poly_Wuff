/*
 * @author Jordan May, x15515673
 *
 */
package com.team.polywuff;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Pedometer extends AppCompatActivity implements SensorEventListener  {

    // Initialise our sensor manager
    private SensorManager sensorManager;

    // we get our values of the X | Y | Z axis
    private float currentY;
    private float currentX;
    private float currentZ;

    // to get our calculation we get the previous X | Y | Z
    private double previousY;
    private double previousX;
    private double previousZ;

    // declare an integer to hold our steps
    private int numberSteps;
    // declare a double to hold our calories
    //
    private double calorie;

    // declare our thresholds to find the limit in which to count a step
    private double thresholdX;
    private double thresholdY;
    private double thresholdZ;

    // Our textViews
    private TextView viewSteps;
    private TextView viewCalories;

    // our buttons
    /*
    private Button start;
    private Button stop;
    private Button reset;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);


        viewSteps = (TextView) findViewById(R.id.stepNumber);
        viewCalories = (TextView) findViewById(R.id.calorieNumber);


        // Initialise our thresholds
        // The values are random and are set based on physical tests
        thresholdX = 6.0;
        thresholdY = 6.0;
        thresholdZ = 6.0;

        currentX = 0;
        currentY = 0;
        currentZ = 0;

        previousX = 0;
        previousY = 0;
        previousZ = 0;

        calorie = 0;
        numberSteps = 0;


    }
    // Method for getting the  X | Y | Z values from the phone
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // grabbing the x y x values at a point in time
            // and assigning it to the variables
            currentX = x;
            currentY = y;
            currentZ = z;

            /*
                Calculation is done by the assumption that
                for every make a person walks
                they burn 0.045 per step
                calories = 0.57 x 175lbs = 99.75 calories per mile
                99.75 / 2,200 = 0.045 calories per step
                The website below is where i got the value:

                www.livestrong.com/article/238020-how-to-convert-pedometer-steps-calories

             */
            if(Math.abs(currentX - previousX) > thresholdX){
                numberSteps++;
                calorie = 0.045 * numberSteps;

                // The decimal format needs api 24
                DecimalFormat t = new DecimalFormat(("##.###"));

                viewSteps.setText(String.valueOf(numberSteps));
                viewCalories.setText(String.valueOf(t.format(calorie)));
            }
            if(Math.abs(currentY - previousY) > thresholdY){
                numberSteps++;
                calorie = 0.045 * numberSteps;

                // The decimal format needs api 24
                DecimalFormat t = new DecimalFormat(("##.###"));

                viewSteps.setText(String.valueOf(numberSteps));
                viewCalories.setText(String.valueOf(t.format(calorie)));
            }
            if(Math.abs(currentZ - previousZ) > thresholdZ){
                numberSteps++;
                calorie = 0.045 * numberSteps;

                // The decimal format needs api 24
                DecimalFormat t = new DecimalFormat(("##.###"));

                viewSteps.setText(String.valueOf(numberSteps));
                viewCalories.setText(String.valueOf(t.format(calorie)));
            }
            // assign to previous x y z
            previousY = y;
            previousX = x;
            previousZ = z;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private void enableSensors(){
        // we register our listener for the accelorometer
        // note we are using the delay fast
        // we use this to get more accurate x y z values at a giving time

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

    }

    public void start(View v){
        // when the presses start
        // make sure all values are set to 0
        numberSteps = 0;
        calorie = 0;

        viewSteps.setText(String.valueOf(numberSteps));
        viewCalories.setText(String.valueOf(calorie));

        // enable our listener
        enableSensors();
    }

    public void reset(View v){
        // when the presses reset
        // make sure all values are set to 0
        numberSteps = 0;
        calorie = 0;
        
        viewSteps.setText(String.valueOf(numberSteps));
        viewCalories.setText(String.valueOf(calorie));

        sensorManager.unregisterListener(sensorEventListener);




    }



    public void onPause(){
        super.onPause();
       // sensorManager.unregisterListener(sensorEventListener);

    }
    protected void onStop(){
        super.onStop();
       // sensorManager.unregisterListener(sensorEventListener);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
