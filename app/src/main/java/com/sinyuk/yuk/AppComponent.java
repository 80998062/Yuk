package com.sinyuk.yuk;

import android.app.Application;
import android.content.Context;

import com.sinyuk.yuk.data.local.ACache;
import com.sinyuk.yuk.data.shot.ShotLocalDataSource;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
    Application getApplication();
    void inject(ShotLocalDataSource target);
}
