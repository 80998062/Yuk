package com.sinyuk.yuk;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sinyuk on 16.6.19.
 */
@Module
public final class AppModule {

    private  Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    Context provideContext() {
        return application;
    }

    @Singleton
    @Provides
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

}
