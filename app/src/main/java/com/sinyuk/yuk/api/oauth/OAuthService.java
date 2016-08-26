package com.sinyuk.yuk.api.oauth;

import com.sinyuk.yuk.api.oauth.AccessToken;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Sinyuk on 16/8/22.
 */
public interface OAuthService {
    @FormUrlEncoded
    @POST("token")
    Observable<AccessToken> getAccessToken(
            @Field("client_id") String id,
            @Field("client_secret") String secret,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri);
}
