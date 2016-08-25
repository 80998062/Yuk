package com.sinyuk.yuk;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.sinyuk.yuk.data.shot.ShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.ShotRepositoryModule;
import com.sinyuk.yuk.utils.Preconditions;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.17.
 */
public class App extends Application implements QbSdk.PreInitCallback {

    private AppComponent appComponent = null;
    private ShotRepositoryComponent shotRepositoryComponent = null;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

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

        QbSdk.allowThirdPartyAppDownload(true);
        TbsDownloader.needDownload(this, false);
        QbSdk.initX5Environment(this,QbSdk.WebviewInitType.FIRSTUSE_AND_PRELOAD,this);
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

    @Override
    public void onCoreInitFinished() {

    }

    @Override
    public void onViewInitFinished(boolean b) {

    }
}
