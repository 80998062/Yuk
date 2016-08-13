package com.sinyuk.yuk.api;

import com.google.gson.Gson;
import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.RetrofitDemo;
import com.sinyuk.yuk.utils.glide.okhttp3.SinyukGlideModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = {ApiModule.class,AppModule.class}, dependencies = {})
public interface ApiComponent {
    Gson getGson();

    OkHttpClient getOkHttpClient();

    DribbleService getDribbleService();

    void inject(RetrofitDemo target);

    void inject(SinyukGlideModule target);
}
