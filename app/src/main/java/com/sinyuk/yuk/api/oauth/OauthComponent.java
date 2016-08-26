package com.sinyuk.yuk.api.oauth;

import com.sinyuk.yuk.ui.login.DribbbleLoginActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by Sinyuk on 16/8/23.
 */
@Singleton
@Subcomponent(
        modules = {
                OauthModule.class
        }
)
public interface OauthComponent {
        void inject(DribbbleLoginActivity target);
}
