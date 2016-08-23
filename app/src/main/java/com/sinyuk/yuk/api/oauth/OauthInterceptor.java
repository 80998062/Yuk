package com.sinyuk.yuk.api.oauth;

import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton public final class OauthInterceptor implements Interceptor {
  private final AccessToken accessToken;

  @Inject public OauthInterceptor(@ForOauth AccessToken accessToken) {
    this.accessToken = accessToken;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder();

      builder.header("Authorization", accessToken.getTokenType() +" "+ accessToken.getAccessToken());

    return chain.proceed(builder.build());
  }
}
