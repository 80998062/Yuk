package com.sinyuk.yuk.ui.feeds;

import com.sinyuk.yuk.ShotsListDemo;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/7/8.
 */
@Singleton
@Component
public interface FeedsFragmentComponent {
    FeedsFragment getFeedsFragment();
    void inject(ShotsListDemo target);
}
