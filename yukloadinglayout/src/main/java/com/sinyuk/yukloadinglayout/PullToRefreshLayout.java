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
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    // If nested scrolling is enabled, the total amount that needed to be
    // consumed by this as the nested scrolling parent is used in place of the
    // overscroll determined by MOVE events in the onTouch handler
    private float mTotalUnconsumed;
    private boolean mNestedScrollInProgress;

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
    private int[] mScrollConsumed;
    private int[] mOffsetInWindow;


    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
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

    public float getmPullHeight() {
        return mPullHeight;
    }

    public float getmHeaderHeight() {
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
        if (!isEnabled() || canChildScrollUp()
                || isRefreshing || mNestedScrollInProgress) {
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

        if (!isEnabled() || canChildScrollUp() || mNestedScrollInProgress) {
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
                if (dispatchNestedPreScroll(0, (int) (mCurrentY - mTouchStartY),mScrollConsumed, mOffsetInWindow))
                {

                }
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
//                    mCurrentY = mTouchStartY;
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

    /******
     * NestedScrollParent
     ********/

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        // 子 View 回调，判断是否开始嵌套滚动 ，
        final boolean isStartNestedScroll = isEnabled() && !isRefreshing && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        Log.d(LOG_TAG, "isStartNestedScroll -> " + isStartNestedScroll);
        return isStartNestedScroll;
    }

    /**
     * 如果 onStartNestedScroll 返回 true ，然后走该方法，这个方法里可以做一些初始化。
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.d(LOG_TAG, "onNestedScrollAccepted");
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    /**
     *  每次滑动前，Child 先询问 Parent 是否需要滑动，
     *  即 dispatchNestedPreScroll()，
     *  这就回调到 Parent 的 onNestedPreScroll()，
     *  Parent 可以在这个回调中“劫持”掉 Child 的滑动，也就是先于 Child 滑动。
     */
    /**
     * @param target 滚动的子视图
     * @param dx 绝对值为手指在x方向滚动的距离，dx<0 表示手指在屏幕向右滚动
     * @param dy 绝对值为手指在y方向滚动的距离，dy<0 表示手指在屏幕向下滚动
     * @param consumed 一个数组，值用来表示父布局消耗了多少距离，未消耗前为[0,0], 如果父布局想处理滚动事件，就可以在这个方法的实现中为consumed[0]，consumed[1]赋值。
     *                 分别表示x和y方向消耗的距离。如父布局想在竖直方向（y）完全拦截子视图，那么让 consumed[1] = dy，就把手指产生的触摸事件给拦截了，子视图便响应不到触摸事件了 。
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.d(LOG_TAG,"onNestedPreScroll dy -> "+dy);
        Log.d(LOG_TAG,"onNestedPreScroll consumed -> "+consumed[1]);
        //向下
        // dy > 0 表示向上滑动
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;// 把子视图位消费的距离全部消费了。
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy; // 消费的 y 轴的距离
                consumed[1] = dy;
            }
            mTotalUnconsumed = MathUtils.constrains(
                    0,
                    mPullHeight * 2,
                    mTotalUnconsumed);

            if (mChildView != null) {
                float offsetY = decelerateInterpolator.getInterpolation(mTotalUnconsumed / mPullHeight / 2) * dy / 2;
                mChildView.setTranslationY(
                        offsetY
                );
                mHeader.getLayoutParams().height = (int) offsetY;
                mHeader.requestLayout();
                if (mPullToRefreshPullingListener != null) {
                    mPullToRefreshPullingListener.onPulling(this, offsetY / mHeaderHeight);
                }
            }
        }

    }


    /**
     * @param target       滚动的子视图
     * @param dxConsumed   手指产生的触摸距离中，子视图消耗的x方向的距离
     * @param dyConsumed   手指产生的触摸距离中，子视图消耗的y方向的距离 ，如果 onNestedPreScroll 中 dy = 20， consumed[0] = 8，那么 dy = 12
     * @param dxUnconsumed 手指产生的触摸距离中，未被子视图消耗的x方向的距离
     * @param dyUnconsumed 手指产生的触摸距离中，未被子视图消耗的y方向的距离
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
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
    }


    /**
     * 响应嵌套滚动结束
     * <p>
     * 当一个嵌套滚动结束后（如MotionEvent#ACTION_UP， MotionEvent#ACTION_CANCEL）会调用该方法，在这里可有做一些收尾工作，比如变量重置
     */
    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        stopNestedScroll();
    }

    /**
     * 返回当前 NestedScrollingParent 的滚动方向，
     *
     * @return
     * @see ViewCompat#SCROLL_AXIS_HORIZONTAL
     * @see ViewCompat#SCROLL_AXIS_VERTICAL
     * @see ViewCompat#SCROLL_AXIS_NONE
     */
    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
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
