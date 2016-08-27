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
import com.sinyuk.yuk.ui.oauth.DribbleOauthActivity;

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


    @Override
    protected int getContentViewID() {
        return R.layout.demo_login;
    }

    @Override
    protected void beforeInflating() {
        Timber.tag("DribbbleLoginActivity");
    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {

    }

    @OnClick(R.id.button)
    public void onClick() {
        startActivity(DribbleOauthActivity.getAuthIntent(this));
    }
}
