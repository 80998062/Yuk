package com.sinyuk.yuk.data.shot;

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
    public ShotLocalDataSource provideShotLocalDataSource() {
        return new ShotLocalDataSource();
    }

    @Singleton
    @Provides
    public ShotRemoteDataSource provideShotRemoteDataSource(DribbleService dribbleService, ShotLocalDataSource shotLocalDataSource) {
        return new ShotRemoteDataSource(dribbleService, shotLocalDataSource);
    }

    @Singleton
    @Provides
    public ShotRepository provideShotRepository(ShotLocalDataSource localDataSource, ShotRemoteDataSource remoteDataSource) {
        return new ShotRepository(localDataSource, remoteDataSource);
    }
}
