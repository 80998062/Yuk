package com.sinyuk.yuk;

import android.app.Application;
import android.content.Context;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.litesuits.orm.LiteOrm;
import com.sinyuk.yuk.data.shot.ShotLocalDataSource;
import com.sinyuk.yuk.utils.scopes.AppScope;

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
    LiteOrm getLiteOrm();
    RxSharedPreferences getPreferences();
    void inject(ShotLocalDataSource target);
}
