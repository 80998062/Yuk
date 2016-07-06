package com.sinyuk.yuk.api;

import com.google.gson.Gson;
import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.RetrofitDemo;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = {ApiModule.class},dependencies = {AppModule.class})
public interface ApiComponent {
    Gson getGson();
    DribbleService getDribbleService();
    void inject(RetrofitDemo target);
}