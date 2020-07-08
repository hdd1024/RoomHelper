package com.hmz.roomhelper;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
