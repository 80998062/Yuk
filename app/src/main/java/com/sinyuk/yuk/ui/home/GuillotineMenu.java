package com.sinyuk.yuk.ui.home;

import android.widget.TextView;

import com.sinyuk.yuk.R;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.ui.oauth.DribbleOauthActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Sinyuk on 16/8/20.
 */
public class GuillotineMenu extends BaseFragment {
    @BindView(R.id.fake_login)
    TextView mFakeLogin;

    @Override
    protected void beforeInflate() {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.home_menu_fragment;
    }

    @Override
    protected void finishInflate() {

    }

    @OnClick(R.id.fake_login)
    public void onClick() {
        getContext().startActivity(DribbleOauthActivity.getAuthIntent(getContext()));
    }
}
