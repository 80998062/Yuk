package com.sinyuk.yuk.api.oauth;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.utils.PrefsUtils;

import java.io.IOException;

import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Singleton
public final class OauthInterceptor implements Interceptor {
    private  RxSharedPreferences mPreferences;

    public OauthInterceptor(RxSharedPreferences preferences) {
        Timber.tag("OauthInterceptor");
        this.mPreferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        final Preference<String> accessToken = mPreferences.getString(PrefsUtils.KEY_ACCESS_TOKEN);
        if (accessToken.isSet()) {
            Timber.d("Add access token : %s", accessToken.get());
            builder.header("Authorization", DribbleApi.ACCESS_TYPE + " " + accessToken.get());
        } else {
            Timber.d("Default access token ");
            builder.header("Authorization", "Bearer a860827f0ea38c1db7d5512d93366499d55424dae8be1f1e0b4065ec6fbeb948");
        }

        return chain.proceed(builder.build());
    }
}
