package com.sinyuk.yuk.utils.lists;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Sinyuk on 16.2.11.
 */
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private int PRELOAD_SIZE = 4;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mIsFirstTimeTouchBottom = true;


    public void setPreloadSize(int PreloadSize) {
        this.PRELOAD_SIZE = PreloadSize;
    }

    public int getPreloadSize() {
        return PRELOAD_SIZE;
    }

    public OnLoadMoreListener(@NonNull StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.mStaggeredGridLayoutManager = staggeredGridLayoutManager;

    }

    public OnLoadMoreListener(@NonNull LinearLayoutManager linearLayout) {
        this.mLinearLayoutManager = linearLayout;

    }

    @Override
    public void onScrolled(RecyclerView rv, int dx, int dy) {
        if (mStaggeredGridLayoutManager != null) {
            boolean isBottom =
                    mStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(
                            new int[2])[1] >=
                            rv.getAdapter().getItemCount() -
                                    PRELOAD_SIZE;

            if (isBottom) {
                if (!mIsFirstTimeTouchBottom) {
                    onLoadMore();
                } else {
                    mIsFirstTimeTouchBottom = false;
                }
            }
        }else if(mLinearLayoutManager != null){
            boolean isBottom =
                    mLinearLayoutManager.findLastCompletelyVisibleItemPosition() >= rv.getAdapter().getItemCount() - PRELOAD_SIZE;

            if (isBottom) {
                if (!mIsFirstTimeTouchBottom) {
                    onLoadMore();
                } else {
                    mIsFirstTimeTouchBottom = false;
                }
            }
        }

    }

    public abstract void onLoadMore();
}
