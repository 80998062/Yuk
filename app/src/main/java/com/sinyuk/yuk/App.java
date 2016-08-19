package com.sinyuk.yuk;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.sinyuk.yuk.data.shot.ShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.ShotRepositoryModule;
import com.sinyuk.yuk.utils.Preconditions;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.17.
 */
public class App extends Application {

    private AppComponent appComponent = null;
    private ShotRepositoryComponent shotRepositoryComponent = null;


    @Override
    public void onCreate() {
        super.onCreate();

        // init Timer
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Stetho.initializeWithDefaults(this);
        LeakCanary.install(this);
        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public ShotRepositoryComponent createShotRepositoryComponent() {
        Preconditions.checkNotNull(appComponent);
        shotRepositoryComponent = appComponent.plus(new ShotRepositoryModule());
        return shotRepositoryComponent;
    }

    public ShotRepositoryComponent getShotRepositoryComponent() {
        if (shotRepositoryComponent == null) {
            createShotRepositoryComponent();
        }
        return shotRepositoryComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
