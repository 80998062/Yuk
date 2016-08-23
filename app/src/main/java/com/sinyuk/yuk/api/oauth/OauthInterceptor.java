package com.sinyuk.yuk.api.oauth;

import com.f2prateek.rx.preferences.Preference;
import com.sinyuk.yuk.api.DribbleApi;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public final class OauthInterceptor implements Interceptor {
    private final Preference<String> accessToken;

    @Inject
    public OauthInterceptor(@ForOauth Preference<String> accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        if (accessToken.isSet()) {
            builder.header("Authorization", DribbleApi.ACCESS_TYPE + " " + accessToken.get());
        }else {
            // TODO: basic oauth

        }

        return chain.proceed(builder.build());
    }
}
