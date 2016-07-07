package com.sinyuk.yuk.data.shot;

import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.api.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/7/6.
 */
@Singleton
@Component(modules = {ShotRepositoryModule.class, ApiModule.class}, dependencies = {AppModule.class})
public interface ShotRepositoryComponent {
    ShotRepository getShotRepository();

    ShotLocalDataSource getShotLocalDataSource();

    ShotRemoteDataSource getShotRemoteDataSource();

}
