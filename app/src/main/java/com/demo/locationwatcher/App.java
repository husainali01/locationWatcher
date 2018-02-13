package com.demo.locationwatcher;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by webwerks1 on 24/10/17.
 */

public class App extends Application {

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //Facebook initialize
        Stetho.initializeWithDefaults(this);
        mInstance = this;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }
}
