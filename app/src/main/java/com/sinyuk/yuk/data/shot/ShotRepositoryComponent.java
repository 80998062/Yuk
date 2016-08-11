package com.sinyuk.yuk.data.shot;

import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.api.ApiModule;
import com.sinyuk.yuk.ui.feeds.FeedsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/7/6.
 */
@Singleton
@Component(modules = {ShotRepositoryModule.class, AppModule.class, ApiModule.class}, dependencies = {})
public interface ShotRepositoryComponent {
    ShotRepository getShotRepository();

    ShotRemoteDataSource getShotRemoteDataSource();

    void inject(FeedsFragment target);
}
