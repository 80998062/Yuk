package com.sinyuk.yuk.api.oauth;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.sinyuk.yuk.BuildConfig;
import com.sinyuk.yuk.api.AccountManager;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.utils.IntentFactory;
import com.sinyuk.yuk.utils.PrefsUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sinyuk on 16/8/23.
 */
@Module
public class OauthModule {

    @Provides
    @Singleton
    @Named("no_cache")
    public OkHttpClient provideOkHttpClientNoCache() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("oauth")
    Retrofit provideRetrofitForAuthorization(Gson gson, @Named("no_cache") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(DribbleApi.OAUTH_END_POINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public OauthService provideAuthorizationService(@Named("oauth") Retrofit retrofit) {
        return retrofit.create(OauthService.class);
    }

    @Provides
    @Singleton
    @ForOauth
    public OauthInterceptor provideOauthInterceptor(@ForOauth Preference<String> accessToken) {
        return new OauthInterceptor(accessToken);
    }

    @Provides
    @Singleton
    @ForOauth
    Preference<String> provideAccessToken(RxSharedPreferences prefs) {
        return prefs.getString(PrefsUtils.KEY_ACCESS_TOKEN);
    }

    @Provides
    @Singleton
    @ForOauth
    AccountManager provideAccountManager(OauthService oauthservice,
                                         SharedPreferences prefs,
                                         IntentFactory factory,
                                         @ForOauth Preference<String> accessToken) {
        return new AccountManager(oauthservice, prefs, factory, accessToken);
    }
}
