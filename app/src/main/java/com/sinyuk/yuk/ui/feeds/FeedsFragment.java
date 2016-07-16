package com.sinyuk.yuk.ui.feeds;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.data.shot.DaggerShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.shot.ShotRepository;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.ListItemMarginDecoration;
import com.sinyuk.yuk.utils.glide.RecyclerViewPreloader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/1.
 */
public class FeedsFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Inject
    ShotRepository shotRepository;
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
        mRecyclerView.postDelayed(() -> loadFeeds(DribbleApi.ALL, 1), 1000);

    }

    private void initRecyclerView() {

        mAdapter = new FeedsAdapter(mContext, Glide.with(this), mShotList);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerViewPreloader<>(mAdapter, mAdapter, 10));

        mRecyclerView.addItemDecoration(new ListItemMarginDecoration(2, R.dimen.content_space_8, true, mContext));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

    }

    public void setFilterType(String type) {
        mPage = 1;
        loadFeeds(type, mPage);
    }

    private void loadFeeds(String type, int page) {
        shotRepository.getShots(type, page)
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        mShotList.addAll(shots);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

}
