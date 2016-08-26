package com.sinyuk.yuk.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.BuildConfig;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.AccountManager;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.api.oauth.OauthModule;
import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.ui.BrowserActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.HttpUrl;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/8/22.
 */
public class DribbbleLoginActivity extends BaseActivity {

    @BindView(R.id.button)
    Button mButton;

    @Inject
    AccountManager mAccountManager;

    @Override
    protected int getContentViewID() {
        return R.layout.demo_login;
    }

    @Override
    protected void beforeInflating() {
        Timber.tag("DribbbleLoginActivity");
        App.get(this).getAppComponent().plus(new OauthModule()).inject(this);
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
                Timber.d("code -> %s", code);
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
                Timber.e("error -> %s", uri.getQueryParameter("error"));
            }
        }
    }


    @OnClick(R.id.button)
    public void onClick() {
        final HttpUrl httpUrl = mAccountManager.createLoginUrl(BuildConfig.DRIBBBLE_CLIENT_ID,
                DribbleApi.REDIRECT_URI,
                DribbleApi.SCOPES);
        Intent intent = new Intent(DribbbleLoginActivity.this, BrowserActivity.class);
        intent.setData(Uri.parse(httpUrl.toString()));
        startActivity(intent);
    }
}
