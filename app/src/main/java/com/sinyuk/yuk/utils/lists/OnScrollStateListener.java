package com.sinyuk.yuk.utils.lists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sinyuk.yuk.utils.ScreenUtils;

/**
 * Created by Sinyuk on 16/8/17.
 */
public class OnScrollStateListener extends RecyclerView.OnScrollListener {
    private final int mTouchSlop;
    private final AppBarBehaviorListener listener;
    private boolean isScrollingDown;

    public OnScrollStateListener(Context context, AppBarBehaviorListener listener) {
        super();
        this.listener = listener;
        mTouchSlop = ScreenUtils.dpToPxInt(context, 16);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.computeVerticalScrollOffset() == 0) {
            listener.onReachTop();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > mTouchSlop && !isScrollingDown) {
            isScrollingDown = true;
            listener.onScrollDown();
        } else if (dy < -mTouchSlop && isScrollingDown) {
            isScrollingDown = false;
            listener.onScrollUp();
        }
    }

    public interface AppBarBehaviorListener {
        void onScrollDown();

        void onScrollUp();

        void onReachTop();
    }
}
