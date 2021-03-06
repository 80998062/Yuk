package com.sinyuk.yuk;

import com.sinyuk.yuk.api.ApiModule;
import com.sinyuk.yuk.api.oauth.OauthComponent;
import com.sinyuk.yuk.api.oauth.OauthModule;
import com.sinyuk.yuk.data.shot.ShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.ShotRepositoryModule;
import com.sinyuk.yuk.ui.BrowserActivity;
import com.sinyuk.yuk.ui.splash.SplashComponent;
import com.sinyuk.yuk.ui.splash.SplashModule;
import com.sinyuk.yuk.utils.glide.okhttp3.SinyukGlideModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/6/30.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    ShotRepositoryComponent plus(ShotRepositoryModule module);

    SplashComponent plus(SplashModule module);

    OauthComponent plus(OauthModule module);

    void inject(BrowserActivity target);

}
