package com.sinyuk.yuk;

import android.util.Log;
import android.widget.RelativeLayout;

import com.sinyuk.yuk.ui.BaseActivity;


import com.sinyuk.yuk.ui.feeds.FeedsFragment;
import com.sinyuk.yuk.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class ShotsListDemo extends BaseActivity {

    @BindView(R.id.root_view)
    RelativeLayout mRootView;

    FeedsFragment feedsFragment = new FeedsFragment();

    @Override
    protected int getContentViewID() {
        return R.layout.activity_shots_list_demo;
    }

    @Override
    protected void beforeInflating() {

    }

    @Override
    protected void finishInflating() {
        if (getSupportFragmentManager() == null) {
            Log.w("Sinyuk -> ", "1");
        }
        if (feedsFragment == null) {
            Log.w("Sinyuk -> ", "2");
        }

        Log.w("Sinyuk -> ", " haha " + getContentViewID());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), feedsFragment, R.id.root_view);
    }
}
