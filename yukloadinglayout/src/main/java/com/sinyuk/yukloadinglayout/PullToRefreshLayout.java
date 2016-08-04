package com.sinyuk.yukloadinglayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * Created by yilun
 * on 09/07/15.
 * <p>
 * <p>
 * Refactor By Sinyuk
 * on 16/8/2.
 * 加入了NestedScrolling相关的接口
 */
class PullToRefreshLayout extends FrameLayout implements NestedScrollingParent, NestedScrollingChild {

    private static final String LOG_TAG = "PullToRefreshLayout";
    private static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(10);
    private float mTouchStartY;
    private float mCurrentY;
    private View mChildView;
    private float mPullHeight;
    private float mHeaderHeight;
    private boolean isRefreshing;
    private PullToRefreshListener mPullToRefreshListener;

    private PullToRefreshPullingListener mPullToRefreshPullingListener;
    private FrameLayout mHeader;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private int INVALID_POINTER = -1;
    private float dy;



    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(false);
        }
    }


    private void init() {
        if (isInEditMode()) {
            return;
        }

        if (getChildCount() > 1) {
            throw new RuntimeException("You can only attach one child");
        }

        mPullHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200,
                getContext().getResources().getDisplayMetrics());

        mHeaderHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                120,
                getContext().getResources().getDisplayMetrics());

        this.post(() -> {
            mChildView = getChildAt(0);
            addHeaderContainer();
        });

    }

    public void setHeaderView(View headerView) {
        post(() -> mHeader.addView(headerView));
    }

    public void setPullHeight(float pullHeight) {
        this.mPullHeight = pullHeight;
    }

    public void setHeaderHeight(float headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    public float getPullHeight() {
        return mPullHeight;
    }

    public float getHeaderHeight() {
        return mHeaderHeight;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    private void addHeaderContainer() {
        FrameLayout headerContainer = new FrameLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.TOP;
        headerContainer.setLayoutParams(layoutParams);

        mHeader = headerContainer;
        addViewInternal(headerContainer);
        setUpChildViewAnimator();
    }

    private void setUpChildViewAnimator() {
        if (mChildView == null) {
            return;
        }
        mChildView.animate().setInterpolator(new DecelerateInterpolator());
        mChildView.animate().setUpdateListener(animation -> {
                    int height = (int) mChildView.getTranslationY();
                    mHeader.getLayoutParams().height = height;
                    mHeader.requestLayout();
                    if (mPullToRefreshPullingListener != null) {
                        mPullToRefreshPullingListener.onReleasing(this, height / mHeaderHeight);
                    }
                }
        );
    }

    private void addViewInternal(@NonNull View child) {
        super.addView(child);
    }

    @Override
    public void addView(@NonNull View child) {
        if (getChildCount() >= 1) {
            throw new RuntimeException("You can only attach one child");
        }
        mChildView = child;
        super.addView(child);
        setUpChildViewAnimator();
    }

    public boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        // positive to check scrolling down.
        return ViewCompat.canScrollVertically(mChildView, -1);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        // 控件可用 || 刷新事件刚结束正在恢复初始状态时 || 子 View 可滚动 || 正在刷新 || 父 View 正在滚动
        if (!isEnabled() || canChildScrollUp() || isRefreshing ) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                mIsBeingDragged = false;
                mTouchStartY = getMotionEventY(e, mActivePointerId);
                if (mTouchStartY == -1) {
                    return false;
                }
                mCurrentY = mTouchStartY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }
                float currentY = getMotionEventY(e, mActivePointerId);
                float dy = currentY - mTouchStartY;
                if (dy > 0 && !canChildScrollUp() && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(e);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent e) {
        if (isRefreshing) {
            return super.onTouchEvent(e);
        }

        if (!isEnabled() || canChildScrollUp()) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = MotionEventCompat.findPointerIndex(e, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                mCurrentY = MotionEventCompat.getY(e, pointerIndex);

                dy = MathUtils.constrains(
                        0,
                        mPullHeight * 2,
                        mCurrentY - mTouchStartY);
                if (mChildView != null) {
                    float offsetY = decelerateInterpolator.getInterpolation(dy / mPullHeight / 2) * dy / 2;
                    mChildView.setTranslationY(
                            offsetY
                    );
                    mHeader.getLayoutParams().height = (int) offsetY;
                    mHeader.requestLayout();
                    if (mPullToRefreshPullingListener != null) {
                        mPullToRefreshPullingListener.onPulling(this, offsetY / mHeaderHeight);
                    }
                }
                return true;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(e);
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                pointerIndex = MotionEventCompat.getActionIndex(e);
                int pointerId = MotionEventCompat.getPointerId(e, pointerIndex);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                //如果触发的点不是活动点的话，就更新mLastX/mLastY，并设其为活动点
                if (pointerId != mActivePointerId) {
                    mActivePointerId = MotionEventCompat.getPointerId(e, pointerIndex);
                    mCurrentY = getMotionEventY(e, mActivePointerId);
                    mTouchStartY = mCurrentY - dy;
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (mChildView != null) {
                    if (mChildView.getTranslationY() >= mHeaderHeight) {
                        mChildView.animate().translationY(mHeaderHeight)
                                .start();
                        isRefreshing = true;
                        if (mPullToRefreshListener != null) {
                            mPullToRefreshListener.onRefresh(this);
                        }
                    } else {
                        mChildView.animate().translationY(0).start();
                    }

                }
                return true;
            default:
                return super.onTouchEvent(e);
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            mCurrentY = getMotionEventY(ev, mActivePointerId);
            mTouchStartY = mCurrentY - dy;
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }




    public void setPullToRefreshListener(PullToRefreshListener pullToRefreshListener) {
        this.mPullToRefreshListener = pullToRefreshListener;
    }

    public void setPullingListener(PullToRefreshPullingListener pullingListener) {
        this.mPullToRefreshPullingListener = pullingListener;
    }

    public void finishRefreshing() {
        if (mChildView != null) {
            mChildView.animate().translationY(0).start();
        }
        isRefreshing = false;
    }


    interface PullToRefreshListener {

        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

    }

    interface PullToRefreshPullingListener {

        void onPulling(PullToRefreshLayout pullToRefreshLayout, float fraction);

        void onReleasing(PullToRefreshLayout pullToRefreshLayout, float fraction);

    }
}
