package com.sinyuk.yuk.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sinyuk.yuk.api.oauth.AccessToken;
import com.sinyuk.yuk.api.oauth.OAuthService;
import com.sinyuk.yuk.utils.IntentFactory;
import com.sinyuk.yuk.utils.PrefsUtils;

import javax.inject.Singleton;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Sinyuk on 16/8/22.
 */
@Singleton
public class AccountManager {


    private final OAuthService oAuthService;
    private final IntentFactory intentFactory;
    private SharedPreferences prefs;
    private boolean isLoggedIn;
    private String accessToken;

    public AccountManager(OAuthService oAuthService, SharedPreferences preferences, IntentFactory intentFactory) {
        this.oAuthService = oAuthService;
        this.prefs = preferences;
        this.intentFactory = intentFactory;

        accessToken = prefs.getString(PrefsUtils.KEY_ACCESS_TOKEN, null);
        isLoggedIn = !TextUtils.isEmpty(accessToken);
        if (isLoggedIn) {
        /*    userId = prefs.getLong(KEY_USER_ID, 0l);
            userName = prefs.getString(KEY_USER_NAME, null);
            userUsername = prefs.getString(KEY_USER_USERNAME, null);
            userAvatar = prefs.getString(KEY_USER_AVATAR, null);
            userType = prefs.getString(KEY_USER_TYPE, null);*/
        }
    }


    public Intent createLoginIntent(@NonNull String id, @NonNull String redirect, @NonNull String scope, @NonNull String state) {

        HttpUrl authorizeUrl = HttpUrl.parse(DribbleApi.OAUTH_END_POINT + DribbleApi.NODE_AUTHORIZE) //
                .newBuilder()
                .addQueryParameter(DribbleApi.PARAM_CLIENT_ID, id)
                .addQueryParameter(DribbleApi.PARAM_REDIRECT_URI, redirect)
                .addQueryParameter(DribbleApi.PARAM_SCOPE, scope)
                .addQueryParameter(DribbleApi.PARAM_STATE, state)
                .build();

        return intentFactory.createUrlIntent(authorizeUrl.toString());
    }

    public void getRequestCode(Intent intent) {
        if (intent != null
                && intent.getData() != null
                && !TextUtils.isEmpty(intent.getData().getAuthority())
                && DribbleApi.REDIRECT_URI.equals(intent.getData().getAuthority())) {
            exchangeAccessToken(intent.getData().getQueryParameter("code"));
        }
    }

    private Observable<AccessToken> exchangeAccessToken(String code) {
        return oAuthService.getAccessToken("", "", code, "")
                .doOnNext(new Action1<AccessToken>() {
                    @Override
                    public void call(AccessToken accessToken) {
                        // save
                    }
                });
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getAccessToken() {
        return !TextUtils.isEmpty(accessToken) ? accessToken
                : /*BuildConfig.DRIBBBLE_CLIENT_ACCESS_TOKEN*/"";
    }

    public void setAccessToken(String accessToken) {
        if (!TextUtils.isEmpty(accessToken)) {
            this.accessToken = accessToken;
            isLoggedIn = true;
            Preferenc
            dispatchLoginEvent();
        }
    }

    public void logout() {
        isLoggedIn = false;
        accessToken = null;
        userId = 0l;
        userName = null;
        userUsername = null;
        userAvatar = null;
        userType = null;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, null);
        editor.putLong(KEY_USER_ID, 0l);
        editor.putString(KEY_USER_NAME, null);
        editor.putString(KEY_USER_AVATAR, null);
        editor.putString(KEY_USER_TYPE, null);
        editor.apply();
        dispatchLogoutEvent();
    }
}
