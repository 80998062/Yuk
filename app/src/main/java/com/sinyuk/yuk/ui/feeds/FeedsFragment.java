package com.sinyuk.yuk.ui.feeds;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.ListItemMarginDecoration;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;

/**
 * Created by Sinyuk on 16/7/1.
 */
@Singleton
public class FeedsFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private FeedsAdapter mAdapter;
    private ArrayList<Shot> mShotList = new ArrayList<>();
    private int mPage;

    @Inject
    public FeedsFragment(){
        // need a default constructor
    }

    @Override
    protected void beforeInflate() {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.feed_list_fragment;
    }

    @Override
    protected void finishInflate() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new FeedsAdapter(mContext, mShotList);

        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addItemDecoration(new ListItemMarginDecoration(2, R.dimen.content_space_8, true, mContext));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

    }

    public void setFilterType(String type){
        mPage = 1;
        loadFeeds(type,mPage);
    }

    private void loadFeeds(String type,int page){

    }
}
