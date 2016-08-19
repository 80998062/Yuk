package com.sinyuk.yuk.data.shot;

import com.sinyuk.yuk.ui.feeds.FeedsFragment;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sinyuk on 16/7/6.
 */
@Singleton
@Subcomponent(
        modules = {
                ShotRepositoryModule.class
        }
)
public interface ShotRepositoryComponent {
    void inject(FeedsFragment target);
}
