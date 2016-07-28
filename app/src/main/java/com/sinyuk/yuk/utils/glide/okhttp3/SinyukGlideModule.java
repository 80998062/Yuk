package com.sinyuk.yuk.utils.glide.okhttp3;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.api.DaggerApiComponent;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by Sinyuk on 16/7/28.
 */
public class SinyukGlideModule implements GlideModule {
    @Inject
    OkHttpClient okHttpClient;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        DaggerApiComponent.builder().appModule(new AppModule((Application) context.getApplicationContext()))
                .build().inject(this);
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
