package com.sinyuk.yuk;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

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

    @Singleton
    @Provides
    public LiteOrm provideLiteOrm() {
        DataBaseConfig config = new DataBaseConfig(application, "yuk.db");
        //"liteorm.db"是数据库名称，名称里包含路径符号"/"则将数据库建立到该路径下，可以使用sd卡路径。 不包含则在系统默认路径下创建DB文件。
        //例如 public static final String DB_NAME = SD_CARD + "/lite/orm/liteorm.db";     DataBaseConfig config = new DataBaseConfig(this, DB_NAME);
        config.dbVersion = 1; // set database version
        config.onUpdateListener = null; // set database update listener
        //独立操作，适用于没有级联关系的单表操作，
        LiteOrm liteOrm = LiteOrm.newSingleInstance(config);
        //级联操作,适用于多表级联操作
        // liteOrm=LiteOrm.newCascadeInstance(config);

        liteOrm.setDebugged(true); // open the log
        return liteOrm;
    }

    @Provides
    @Singleton
    public RxSharedPreferences providePreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
        return RxSharedPreferences.create(preferences);
    }


}
