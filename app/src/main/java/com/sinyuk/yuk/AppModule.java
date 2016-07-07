package com.sinyuk.yuk;

import android.app.Application;
import android.content.Context;

import com.sinyuk.yuk.data.local.ACache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sinyuk on 16.6.19.
 */
@Module
public final class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return application;
    }

}
