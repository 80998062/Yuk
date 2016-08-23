package com.sinyuk.yuk.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.utils.PrefsUtils;

import java.util.prefs.Preferences;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Sinyuk on 16/8/22.
 */
public class AccountManager {

    private final Context hostActivity;

    private final OAuthService oAuthService;
    private SharedPreferences prefs;
    private boolean isLoggedIn;
    private String accessToken;

    public AccountManager(Context activity, OAuthService oAuthService, SharedPreferences preferences) {
        this.hostActivity = activity;
        this.oAuthService = oAuthService;
        this.prefs = preferences;

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

    public void requestDribbbleAccess(@NonNull String id,
                                      @NonNull String redirect,
                                      @NonNull String scope,
                                      @NonNull String state) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(DribbleApi.OAUTH_END_POINT +
                        DribbleApi.NODE_AUTHORIZE
                        + "?" + DribbleApi.PARAM_CLIENT_ID + "=" + id
                        + "?" + DribbleApi.PARAM_REDIRECT_URI + "=" + redirect
                        + "?" + DribbleApi.PARAM_SCOPE + "=" + scope
                        + "?" + DribbleApi.PARAM_STATE + "=" + state));

        hostActivity.startActivity(intent);
    }
    
    public void getRequestCode(Intent intent){
        if (intent != null
                && intent.getData() != null
                && !TextUtils.isEmpty(intent.getData().getAuthority())
                && DribbleApi.REDIRECT_URI.equals(intent.getData().getAuthority())) {
            exchangeAccessToken(intent.getData().getQueryParameter("code"));
        }
    }

    private Observable<AccessToken> exchangeAccessToken(String code) {
        return oAuthService.getAccessToken("","",code,"")
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
