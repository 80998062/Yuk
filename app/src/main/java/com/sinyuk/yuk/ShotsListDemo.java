package com.sinyuk.yuk;

import android.widget.RelativeLayout;

import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.utils.ActivityUtils;

import butterknife.BindView;

public class ShotsListDemo extends BaseActivity {

    @BindView(R.id.root_view)
    RelativeLayout mRootView;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_shots_list_demo;
    }

    @Override
    protected void beforeInflating() {

    }

    @Override
    protected void finishInflating() {
        ActivityUtils.addFragmentToActivity(,getContentViewID());
    }
}
