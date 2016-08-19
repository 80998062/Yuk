package com.sinyuk.yuk;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.ui.feeds.FeedsFragment;
import com.sinyuk.yuk.utils.ActivityUtils;
import com.sinyuk.yuk.utils.ScreenUtils;
import com.sinyuk.yuk.utils.lists.OnScrollStateListener;

import butterknife.BindView;
import timber.log.Timber;

public class ShotsListDemo extends BaseActivity implements OnScrollStateListener.AppBarBehaviorListener {

    @BindView(R.id.root_view)
    CoordinatorLayout mRootView;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
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
        final int max = mAppBarLayout.getTotalScrollRange();
        /**
         * 首先这时候上面那个还==0
         * 然后可以用throllteLast什么的 或者 debounce
         */
        Timber.e("max %d ", max);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {;

            }
        });
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
