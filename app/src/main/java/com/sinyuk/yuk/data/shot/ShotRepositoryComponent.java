package com.sinyuk.yuk.data.shot;

import com.sinyuk.yuk.api.ApiComponent;
import com.sinyuk.yuk.ui.feeds.FeedsFragment;
import com.sinyuk.yuk.utils.scopes.PerActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/7/6.
 */
@PerActivity
@Component(modules = {ShotRepositoryModule.class}, dependencies = {ApiComponent.class})
public interface ShotRepositoryComponent {
    ShotRepository getShotRepository();

    ShotLocalDataSource getShotLocalDataSource();

    ShotRemoteDataSource getShotRemoteDataSource();

    void inject(FeedsFragment target);
}
