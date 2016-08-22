package com.sinyuk.yuk.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.ui.BaseActivity;

/**
 * Created by Sinyuk on 16/8/22.
 */
public class DribbbleLoginActivity extends BaseActivity{

    @Override
    protected int getContentViewID() {
        return 0;
    }

    @Override
    protected void beforeInflating() {

    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(DribbleApi.REDIRECT_URI)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                // we'll do that in a minute
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}
