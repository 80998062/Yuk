package com.sinyuk.yuk.api;

import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.RetrofitDemo;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = {AppModule.class,ApiModule.class})
public interface ApiComponent {
    void inject(RetrofitDemo target);
}
