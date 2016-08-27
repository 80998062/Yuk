package com.sinyuk.yuk.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.AccountManager;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.api.oauth.OauthModule;
import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.ui.BrowserActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
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
        checkAuthCallback(getIntent());
    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkAuthCallback(intent);
    }

    private void checkAuthCallback(Intent intent) {
//        yuk://oauth-callback?code=
        if (intent.getData() == null || !intent.getData().getScheme().equals("yuk"))
            return;
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(DribbleApi.REDIRECT_CALLBACK)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (!TextUtils.isEmpty(code)) {
                // get access token
                // we'll do that in a minute
                mAccountManager.onReceiveRequestCode(code);
                Timber.d("code -> %s", code);
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
                Timber.e("error -> %s", uri.getQueryParameter("error"));
            }
        }

    }

    @OnClick(R.id.button)
    public void onClick() {
        Intent intent = new Intent(DribbbleLoginActivity.this, BrowserActivity.class);
        intent.setData(Uri.parse(DribbleApi.LOGIN_URL));
        Timber.d("ask for request code -> %s", Uri.parse(DribbleApi.LOGIN_URL).getPath());
        startActivity(intent);
    }
}
