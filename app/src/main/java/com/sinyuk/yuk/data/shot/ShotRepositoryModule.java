package com.sinyuk.yuk.data.shot;

import com.f2prateek.rx.preferences.RxSharedPreferences;
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
    public ShotRemoteDataSource provideShotRemoteDataSource(DribbleService dribbleService) {
        return new ShotRemoteDataSource(dribbleService);
    }

    @Singleton
    @Provides
    public ShotRepository provideShotRepository(ShotRemoteDataSource remoteDataSource,
                                                RxSharedPreferences preferences) {
        return new ShotRepository(remoteDataSource, preferences);
    }
}
