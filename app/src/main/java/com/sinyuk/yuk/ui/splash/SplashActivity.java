package com.sinyuk.yuk.ui.splash;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.ui.BaseActivity;

/**
 * Created by Sinyuk on 16/8/16.
 */
public class SplashActivity extends BaseActivity{

    @Override
    protected int getContentViewID() {
        return 0;
    }

    @Override
    protected void beforeInflating() {
        App.get(this).getAppComponent().plus(new SplashModule(this)).inject(this);
    }

    @Override
    protected void finishInflating() {

    }
}
