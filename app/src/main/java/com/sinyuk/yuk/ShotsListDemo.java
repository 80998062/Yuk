package com.sinyuk.yuk;

import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.ui.feeds.FeedsFragment;
import com.sinyuk.yuk.utils.ActivityUtils;
import com.sinyuk.yuk.utils.lists.OnScrollStateListener;

import butterknife.BindView;
import timber.log.Timber;

public class ShotsListDemo extends BaseActivity implements OnScrollStateListener.AppBarBehaviorListener {

    @BindView(R.id.root_view)
    RelativeLayout mRootView;

    FeedsFragment feedsFragment = new FeedsFragment();
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.list_view_container)
    FrameLayout mListViewContainer;



    @Override
    protected int getContentViewID() {
        return R.layout.activity_shots_list_demo;
    }

    @Override
    protected void beforeInflating() {
        Timber.tag("ShotsListDemo");
    }

    @Override
    protected void finishInflating() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), feedsFragment, R.id.list_view_container);
//        mYukLoadingLayout.setRefreshListener(jellyRefreshLayout -> jellyRefreshLayout.postDelayed((Runnable) jellyRefreshLayout::finishRefreshing, 6000));
    }

    @Override
    public void onScrollDown() {
        Timber.d("onScrollDown");
    }

    @Override
    public void onScrollUp() {
        Timber.d("onScrollUp");
    }

    @Override
    public void onReachTop() {
        Timber.d("onReachTop");
    }
}
