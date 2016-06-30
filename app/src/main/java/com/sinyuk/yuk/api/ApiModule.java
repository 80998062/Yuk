package com.sinyuk.yuk.api;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sinyuk.yuk.BuildConfig;
import com.sinyuk.yuk.utils.NetWorkUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sinyuk on 16/6/24.
 */
@Module
public class ApiModule {

  /*  private final Application application;

    public ApiModule(Application application){
        this.application = application;
    }*/

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                // All timestamps are returned in ISO 8601 format:
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                // Blank fields are included as null instead of being omitted.
                .serializeNulls()
                .create();
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application application) {
        File cacheFile = new File(application.getExternalCacheDir(), "okhttp_cache");

        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }

        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!NetWorkUtils.isNetworkConnection(application)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (!NetWorkUtils.isNetworkConnection(application)) {
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 0)
//                        .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                // 无网络时，设置超时为1周
                int maxStale = 60 * 60 * 24 * 7;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("nyn")
                        .build();
            }
            return response;
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);

        // 请求头
        Interceptor authorization = chain -> {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer a860827f0ea38c1db7d5512d93366499d55424dae8be1f1e0b4065ec6fbeb948")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(originalRequest.method(), originalRequest.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);

        };

        //设置头
        builder.addInterceptor(authorization);

        //设置超时
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        // 分页设置
        Interceptor paginationInterceptor = chain -> {
            Request originalRequest = chain.request();
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("per_page", "10")
                    .build();
            Request request = originalRequest.newBuilder().url(modifiedUrl).build();
            return chain.proceed(request);
        };
        builder.addInterceptor(paginationInterceptor);

        return builder.build();
    }


    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson,OkHttpClient okHttpClient){
      return new Retrofit.Builder()
              .baseUrl(DribbleApi.END_POINT)
              .addConverterFactory(GsonConverterFactory.create(gson))
              .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
              .client(okHttpClient)
              .build();
    }

    @Provides
    @Singleton
    public DribbleService provideDribbleService(Retrofit retrofit){
        return retrofit.create(DribbleService.class);
    }


}
