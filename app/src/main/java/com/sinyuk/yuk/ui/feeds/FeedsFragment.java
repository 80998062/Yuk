package com.sinyuk.yuk.ui.feeds;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.DaggerShotRepositoryComponent;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.shot.ShotRepository;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.ListItemMarginDecoration;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;

/**
 * Created by Sinyuk on 16/7/1.
 */
public class FeedsFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    //    @Inject
//    ShotRepository shotRepository;
    private FeedsAdapter mAdapter;
    private ArrayList<Shot> mShotList = new ArrayList<>();
    private int mPage;

    @Inject
    ShotRepository shotRepository;

    public FeedsFragment() {
        // need a default constructor
    }

    @Override
    protected void beforeInflate() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DaggerShotRepositoryComponent.builder()
                .apiComponent(((App) activity.getApplication()).getApiComponent())
                .build().inject(this);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.feed_list_fragment;
    }

    @Override
    protected void finishInflate() {
//        initRecyclerView();
/*        shotRepository.getShots("", 1).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.w("Sinyuk -> ", throwable.getLocalizedMessage());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Shot>>() {
                    @Override
                    public void call(List<Shot> shots) {
                        for (Shot shot : shots) {
                            Log.w("Sinyuk -> ", shot.toString());
                        }
                    }
                });*/
    }

    private void initRecyclerView() {
        mAdapter = new FeedsAdapter(mContext, mShotList);

        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addItemDecoration(new ListItemMarginDecoration(2, R.dimen.content_space_8, true, mContext));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

    }

    public void setFilterType(String type) {
        mPage = 1;
        loadFeeds(type, mPage);
    }

    private void loadFeeds(String type, int page) {

    }
}
