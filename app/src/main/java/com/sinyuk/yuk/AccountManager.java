package com.sinyuk.yuk;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sinyuk.yuk.api.DribbleApi;

/**
 * Created by Sinyuk on 16/8/22.
 */
public class AccountManager {
    public Intent requestDribbbleAccess(@NonNull String id,
                                        @NonNull String redirect,
                                        @NonNull String scope,
                                        @NonNull String state) {
        return new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(DribbleApi.OAUTH_END_POINT +
                        DribbleApi.NODE_AUTHORIZE
                        + "?" + DribbleApi.PARAM_CLIENT_ID + "=" + id
                        + "?" + DribbleApi.PARAM_REDIRECT_URI + "=" + redirect
                        + "?" + DribbleApi.PARAM_SCOPE + "=" + scope
                        + "?" + DribbleApi.PARAM_STATE + "=" + state));
    }
}
