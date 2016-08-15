package com.sinyuk.yuk.ui.feeds;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.sinyuk.yuk.utils.lists.OnLoadMoreListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/1.
 */
public class FeedsFragment extends BaseFragment {
    private static final int FIRST_PAGE = 1;
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
    private String mType = DribbleApi.ALL;


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
        mRecyclerView.postDelayed(() -> loadFeeds(FIRST_PAGE), 3000);
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
            }
        });

        mRecyclerView.addOnScrollListener(new OnLoadMoreListener(layoutManager) {
            @Override
            public void onLoadMore() {
                loadFeeds(mPage);
            }
        });

    }

    /**
     * Start and show loading more progress bar
     */
    private void showLoadingProgress() {
        mSmoothProgressBar.setVisibility(View.VISIBLE);
        mSmoothProgressBar.progressiveStart();
    }

    /**
     * Contrary to the above method
     */
    private void hideLoadingProgress() {
        mSmoothProgressBar.setVisibility(View.GONE);
        mSmoothProgressBar.progressiveStop();
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        mViewAnimator.setDisplayedChildId(R.id.layout_error);
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFilterType(String type) {
        loadFeeds(FIRST_PAGE);
    }

    private void loadFeeds(int page) {
        addSubscription(
                shotRepository.getShots(mType, page)
                        .doOnSubscribe(this::showLoadingProgress)
                        .doOnError(this::handleError)
                        .doOnCompleted(() -> mPage++)
                        .doAfterTerminate(this::hideLoadingProgress)
                        .subscribe(mAdapter));
    }


}
