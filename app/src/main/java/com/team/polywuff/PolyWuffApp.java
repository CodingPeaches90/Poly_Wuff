package com.team.polywuff;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;

/**
 * Created by jordanmay on 28/02/2017.
 *
 * @reference: https://developers.facebook.com/docs/app-events/android/
 * @reference: https://www.youtube.com/watch?v=Jims30XAzjI
 */

public class PolyWuffApp extends Application {

    // this class is from the facebook developer page
    // it is used for event loggin i.e user demographics etc

    @Override
    public void onCreate(){
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }
}
