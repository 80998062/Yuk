package com.sinyuk.yuk.ui.feeds;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.data.shot.DaggerShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.shot.ShotRepository;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.BetterViewAnimator;
import com.sinyuk.yuk.utils.PrefsUtils;
import com.sinyuk.yuk.utils.lists.ListItemMarginDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/1.
 */
public class FeedsFragment extends BaseFragment {
    @Inject
    ShotRepository shotRepository;
    @Inject
    RxSharedPreferences mSharedPreferences;

    @BindView(R.id.loading_layout)
    RelativeLayout mLoadingLayout;

    @BindView(R.id.layout_list)
    RelativeLayout mListLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    SmoothProgressBar mSmoothProgressBar;

    @BindView(R.id.layout_error)
    RelativeLayout mLayoutError;

    @BindView(R.id.layout_empty)
    RelativeLayout mLayoutEmpty;

    @BindView(R.id.view_animator)
    BetterViewAnimator mViewAnimator;

    private FeedsAdapter mAdapter;
    private ArrayList<Shot> mShotList = new ArrayList<>();
    private int mPage;


    public FeedsFragment() {
        // need a default constructor
    }

    @Override
    protected void beforeInflate() {
        Timber.tag("FeedsFragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DaggerShotRepositoryComponent.builder()
                .appModule(new AppModule(activity.getApplication()))
                .build().inject(this);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.feed_list_fragment;
    }

    @Override
    protected void finishInflate() {
        initRecyclerView();
        mRecyclerView.postDelayed(() -> loadFeeds(DribbleApi.ANIMATED, 1), 0);
    }

    private void initRecyclerView() {

        mAdapter = new FeedsAdapter(mContext, Glide.with(this), mShotList);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new ListItemMarginDecoration(2, R.dimen.content_space_8, true, mContext));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mSharedPreferences.getBoolean(PrefsUtils.auto_play_gif, false)
                .asObservable()
                .subscribe(autoPlayGif -> {
                    mAdapter.setAutoPlayGif(autoPlayGif);
                });

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mViewAnimator.setDisplayedChildId(mAdapter.getItemCount() == 0
                        ? R.id.layout_empty //
                        : R.id.layout_list);
                /*swipeRefreshView.setRefreshing(false);*/
            }
        });

    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFilterType(String type) {
        mPage = 1;
        loadFeeds(type, mPage);
    }

    private void loadFeeds(String type, int page) {
        shotRepository.getShots(type, page)
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        if (page == 1 && !mShotList.isEmpty()) { mShotList.clear(); }
                        mShotList.addAll(shots);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
    
}
