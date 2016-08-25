package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.shot.ShotRepository;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.BetterViewAnimator;
import com.sinyuk.yuk.utils.BlackMagics;
import com.sinyuk.yuk.utils.PrefsKeySet;
import com.sinyuk.yuk.utils.lists.OnScrollStateListener;
import com.sinyuk.yukloadinglayout.YukLoadingLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.Observer;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/1.
 */
public class FeedsFragment extends BaseFragment {
    private static final int FIRST_PAGE = 1;
    private static final int PRELOAD_THRESHOLD = 2;

    @Inject
    ShotRepository shotRepository;
    @Inject
    RxSharedPreferences mSharedPreferences;
    @BindView(R.id.layout_list)
    YukLoadingLayout mYukLoadingLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    SmoothProgressBar smoothProgressBar;
    @BindView(R.id.layout_error)
    RelativeLayout mLayoutError;
    @BindView(R.id.layout_loading)
    RelativeLayout mLayoutLoading;
    @BindView(R.id.view_animator)
    BetterViewAnimator mViewAnimator;
    private FeedsAdapter mAdapter;


    private int mPage = FIRST_PAGE;

    private final Observer<List<Shot>> addFeedsToList = new Observer<List<Shot>>() {
        @Override
        public void onCompleted() {
            mPage = mPage + 1;
        }

        @Override
        public void onError(Throwable e) {
            handleError(e);
        }

        @Override
        public void onNext(List<Shot> shots) {
            mAdapter.setDataSet(shots);
            Timber.d("Data in adapter %s", shots.toString());
        }
    };
    private String mType = DribbleApi.ALL;
    private boolean isLoading;

    public FeedsFragment() {
        // need a default constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getShotRepositoryComponent().inject(this);
    }

    @Override
    protected void beforeInflate() {
        Timber.tag("FeedsFragment");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.feed_list_fragment;
    }

    @Override
    protected void finishInflate() {
        setupLoadingLayout();
        initRecyclerView();
        initData();
    }

    private void setupLoadingLayout() {
        mYukLoadingLayout.setRefreshListener(yukLoadingLayout -> {
            refreshFeeds();
        });
    }

    private void initRecyclerView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new FeedsItemDecoration(getContext()));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isLoading) { return; }
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isBottom =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - PRELOAD_THRESHOLD;
                if (isBottom) { loadFeeds(mPage); }
            }
        });

        mRecyclerView.addOnScrollListener(new OnScrollStateListener(getContext(), (OnScrollStateListener.AppBarBehaviorListener) mContext));


    }

    private void setupProgressBar() {
        if (null == smoothProgressBar) {
            final View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.feed_layout_list_footer, mRecyclerView, false);
            mAdapter.setFooterView(loadingView);
            smoothProgressBar = (SmoothProgressBar) loadingView.findViewById(R.id.progress_bar);
        }
    }

    private void initData() {

        mAdapter = new FeedsAdapter(getContext(), Glide.with(this));

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mViewAnimator.setDisplayedChildId(mAdapter.getDataItemCount() == 0 ? R.id.layout_loading : R.id.layout_list);
            }
        });

        mSharedPreferences.getBoolean(PrefsKeySet.KEY_AUTO_PLAY_GIF, false)
                .asObservable()
                .subscribe(autoPlayGif -> {
                    mAdapter.setAutoPlayGif(autoPlayGif);
                });

        refreshFeeds();

        setupProgressBar();
    }

    /**
     * Start and show loading more progress bar
     */
    private void showLoadingProgress() {
        isLoading = true;
        BlackMagics.scrollUp(smoothProgressBar).withStartAction(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            smoothProgressBar.progressiveStart();
        });

    }

    /**
     * Contrary to the above method
     */
    private void hideLoadingProgress() {
        isLoading = false;
        if (mPage == FIRST_PAGE) { return; } // 当加载第一页时 什么都不做
        BlackMagics.scrollDown(smoothProgressBar).withEndAction(() -> {
            smoothProgressBar.setVisibility(View.GONE);
            smoothProgressBar.progressiveStop();
        });
    }

    /**
     * 刷新的时候延迟三秒为了完整的展示动画
     * 临时这么搞搞 有待优化
     */
    private void hideRefreshView() {
        if (mYukLoadingLayout != null && mYukLoadingLayout.isRefreshing()) {
            mYukLoadingLayout.postDelayed(() -> mYukLoadingLayout.finishRefreshing(), 3000);
        }
    }

    /**
     * 加载错误时
     *
     * @param throwable
     */
    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        if (mYukLoadingLayout != null && mYukLoadingLayout.isRefreshing()) {
            mYukLoadingLayout.postDelayed(() -> mYukLoadingLayout.finishRefreshing(), 3000);
            mYukLoadingLayout.postDelayed(() -> mViewAnimator.setDisplayedChildId(R.id.layout_error), 3500);
        } else {
            mViewAnimator.setDisplayedChildId(R.id.layout_error);
        }

    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFilterType(String type) {
        loadFeeds(FIRST_PAGE);
    }

    private void loadFeeds(int page) {
        addSubscription(
                shotRepository.getShots(mType, page)
                        .doOnSubscribe(this::showLoadingProgress)
                        .doAfterTerminate(this::hideLoadingProgress)
                        .subscribe(addFeedsToList));
    }

    private void refreshFeeds() {
        addSubscription(
                shotRepository.getShots(mType, FIRST_PAGE)
                        .doAfterTerminate(this::hideRefreshView)
                        .doAfterTerminate(() -> mPage = FIRST_PAGE)
                        .subscribe(addFeedsToList));
    }


}
