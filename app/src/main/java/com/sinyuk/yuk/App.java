package com.sinyuk.yuk;

import android.app.Application;

import com.sinyuk.yuk.api.DribbleClient;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.17.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        DribbleClient.initRetrofit(this);


    }

}
