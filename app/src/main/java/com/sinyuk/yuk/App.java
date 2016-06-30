package com.sinyuk.yuk;

import android.app.Application;

import com.sinyuk.yuk.api.ApiComponent;
import com.sinyuk.yuk.api.ApiModule;
import com.sinyuk.yuk.api.DaggerApiComponent;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.17.
 */
public class App extends Application {

    private ApiComponent mApiComponent = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // init Timer
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public ApiComponent getApiComponent() {
        if (mApiComponent == null) {
            mApiComponent = DaggerApiComponent.builder()
                    .appModule(new AppModule(this))
                    .apiModule(new ApiModule(this))
                    .build();
        }
        return mApiComponent;
    }


}
