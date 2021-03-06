package com.sinyuk.yuk.api;

import android.text.TextUtils;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.BuildConfig;
import com.sinyuk.yuk.api.oauth.AccessToken;
import com.sinyuk.yuk.api.oauth.OauthService;
import com.sinyuk.yuk.data.user.User;
import com.sinyuk.yuk.utils.PrefsKeySet;

import javax.inject.Singleton;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/8/22.
 */
@Singleton
public class AccountManager {

    private final DribbleService mDribbleService;
    private final OauthService mOauthService;
    private Preference<String> userName;
    private Preference<String> userUsername;
    private Preference<String> userAvatar;
    private Preference<String> userType;
    private Preference<Long> userId;
    private RxSharedPreferences mRxSharedPreferences;
    private Preference<String> mAccessToken;

    public AccountManager(DribbleService dribbleService, OauthService oauthService, RxSharedPreferences rxSharedPreferences) {
        Timber.tag("AccountManager");
        this.mDribbleService = dribbleService;
        this.mOauthService = oauthService;
        this.mRxSharedPreferences = rxSharedPreferences;
        mAccessToken = mRxSharedPreferences.getString(PrefsKeySet.KEY_ACCESS_TOKEN);


        userId = mRxSharedPreferences.getLong(PrefsKeySet.KEY_USER_ID, 0L);
        userName = mRxSharedPreferences.getString(PrefsKeySet.KEY_USER_NAME, null);
        userUsername = mRxSharedPreferences.getString(PrefsKeySet.KEY_USER_USERNAME, null);
        userAvatar = mRxSharedPreferences.getString(PrefsKeySet.KEY_USER_AVATAR, null);
        userType = mRxSharedPreferences.getString(PrefsKeySet.KEY_USER_TYPE, null);
    }

    public Observable<AccessToken> getAccessToken(String code) {
        return mOauthService.getAccessToken(BuildConfig.DRIBBBLE_CLIENT_ID, BuildConfig.DRIBBBLE_CLIENT_SECRET, code, DribbleApi.REDIRECT_URI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<AccessToken>() {
                    @Override
                    public void call(AccessToken accessToken) {
                        saveAccessToken(accessToken);
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        refreshUserProfile();
                    }
                });
    }

    public Observable<User> refreshUserProfile() {
        Timber.d("refreshUserProfile");
        return mDribbleService.getAuthenticatedUser()
                .doOnNext(this::saveInPreference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void saveInPreference(User user) {
        Timber.d("saveInPreference");
        Timber.d(user.toString());
        userName.set(user.getName());
        userUsername.set(user.getUsername());
        userAvatar.set(user.getAvatarUrl());
        userType.set(user.getType());
        userId.set(user.getId());
    }

    public Observable<User> getCurrentUser() {
        return Observable.just(new User()).map(user -> {
            user.setName(userName.get());
            user.setUsername(userUsername.get());
            user.setAvatarUrl(userAvatar.get());
            user.setType(userType.get());
            user.setId(userId.get());
            return user;
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }

    public boolean isLoggedIn() {
        mAccessToken = mRxSharedPreferences.getString(PrefsKeySet.KEY_ACCESS_TOKEN);
        return mAccessToken.isSet();
    }

    public void saveAccessToken(AccessToken accessToken) {
        Timber.d("Get AccessToken : %s", accessToken.toString());
        if (!TextUtils.isEmpty(accessToken.getAccessToken())) {
            mAccessToken.set(accessToken.getAccessToken());
        }
    }

    public Observable<Void> logout() {
        return Observable.empty().map((Func1<Object, Void>) o -> {
            mAccessToken.delete();
            userId.delete();
            userName.delete();
            userUsername.delete();
            userAvatar.delete();
            userType.delete();
            return null;
        }).doOnNext(aVoid -> {
            // TODO: post a event
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
}
