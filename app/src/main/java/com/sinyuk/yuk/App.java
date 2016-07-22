package com.sinyuk.yuk;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.sinyuk.yuk.api.ApiComponent;
import com.sinyuk.yuk.api.DaggerApiComponent;


import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.17.
 */
public class App extends Application {

    private ApiComponent mApiComponent = null;
    private AppComponent mAppComponent = null;
    private AppModule mAppModule = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // init Timer
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Stetho.initializeWithDefaults(this);

        createAppComponent();
    }

    private void createAppComponent() {
        if (mAppComponent == null) {
            mAppModule = new AppModule(this);
            mAppComponent = DaggerAppComponent.builder().appModule(mAppModule).build();
        }
    }

    public ApiComponent getApiComponent() {
        if (mAppModule != null) {
            mApiComponent = DaggerApiComponent.builder().appModule(mAppModule).build();
        }
        return mApiComponent;
    }


}
