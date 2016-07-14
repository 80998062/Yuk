package com.sinyuk.yuk.data.shot;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.litesuits.orm.LiteOrm;
import com.sinyuk.yuk.api.DribbleService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sinyuk on 16/7/6.
 */
@Module
public class ShotRepositoryModule {
    @Singleton
    @Provides
    public ShotLocalDataSource provideShotLocalDataSource(LiteOrm liteOrm) {
        return ShotLocalDataSource.getInstance(liteOrm);
    }

    @Singleton
    @Provides
    public ShotRemoteDataSource provideShotRemoteDataSource(DribbleService dribbleService, ShotLocalDataSource shotLocalDataSource) {
        return new ShotRemoteDataSource(dribbleService, shotLocalDataSource);
    }

    @Singleton
    @Provides
    public ShotRepository provideShotRepository(ShotLocalDataSource localDataSource,
                                                ShotRemoteDataSource remoteDataSource,
                                                RxSharedPreferences preferences) {
        return new ShotRepository(localDataSource, remoteDataSource, preferences);
    }
}
