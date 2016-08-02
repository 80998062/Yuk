package com.sinyuk.yukloadinglayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sinyuk on 16/8/1.
 */
public class YukLoadingLayout extends PullToRefreshLayout {

    private static final long RETURN_DURATION = 300;
    private static final float BICYCLE_RAISE_TOP = 75;
    private static final long BICYCLE_RAISE_DURATION = 200;
    private static final float BICYCLE_START_BOTTOM = 50;
    private static final long BICYCLE_FALL_DURATION = 150;
    private static final long TEXT_SHOW_DURATION = 200;
    OnRefreshListener mJellyRefreshListener;

    private String[] dotsArray = {"... ", "..  ", ".   ", "    "};

    private int mJellyColor;
    private AnimatedVectorDrawableCompat bicycleDrawable;
    private JellyViewFrameLayout jellyView;
    private ImageView animatedBicycle;
    private TextView loadingText;
    private ValueAnimator dotsAnimator;

    public YukLoadingLayout(Context context) {
        super(context);
        setupHeader();
    }

    public YukLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
        setupHeader();
    }

    public YukLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(attrs);
        setupHeader();
    }

    public YukLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttributes(attrs);
        setupHeader();
    }

    private void setAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.YukLoadingLayout);
        try {
            Resources resources = getResources();
            mJellyColor = a.getColor(R.styleable.YukLoadingLayout_jellyColor,
                    resources.getColor(android.R.color.white));
        } finally {
            a.recycle();
        }
    }


    public void setRefreshListener(OnRefreshListener onRefreshListener) {
        this.mJellyRefreshListener = onRefreshListener;
    }

    private void setupHeader() {
        if (isInEditMode()) {
            return;
        }

        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(getContext()).inflate(R.layout.new_view_pull_header, null);
        jellyView = (JellyViewFrameLayout) headerView.findViewById(R.id.jelly);
        animatedBicycle = (ImageView) headerView.findViewById(R.id.loading_iv);
        bicycleDrawable = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.animated_bicycle);
        if (animatedBicycle != null) {
            animatedBicycle.setImageDrawable(bicycleDrawable);
        }

        loadingText = (TextView) headerView.findViewById(R.id.loading_tv);
        jellyView.setJellyColor(mJellyColor);
        final float headerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        setHeaderHeight(headerHeight);
        final float pullHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        setPullHeight(pullHeight);
        setHeaderView(headerView);
        // 这个估计是下拉到肯定要刷新才会触发的
        setPullToRefreshListener(
                pullToRefreshLayout -> {
                    if (mJellyRefreshListener != null) {
                        mJellyRefreshListener.onRefresh(YukLoadingLayout.this);
                    }
                    jellyView.setMinimumHeight((int) (headerHeight));
                    ValueAnimator animator = ValueAnimator.ofInt(jellyView.getJellyHeight(), 0);
                    animator.addUpdateListener(animation -> {
                        jellyView.setJellyHeight((int) animation.getAnimatedValue());
                    });
                    animator.setInterpolator(new OvershootInterpolator(6));
                    animator.setDuration(RETURN_DURATION);
                    animator.start();

                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startBicycleAnimation(animatedBicycle);
                        }
                    });
                }
        );
        setPullingListener(new PullToRefreshLayout.PullToRefreshPullingListener() {
            @Override
            public void onPulling(PullToRefreshLayout pullToRefreshLayout, float fraction) {
                toggleVisibility(View.GONE);
                jellyView.setMinimumHeight((int) (headerHeight * MathUtils.constrains(0, 1, fraction)));
                jellyView.setJellyHeight((int) (pullHeight * Math.max(0, fraction - 1)));
            }

            @Override
            public void onReleasing(PullToRefreshLayout pullToRefreshLayout, float fraction) {
                if (!pullToRefreshLayout.isRefreshing()) {
                    toggleVisibility(View.GONE);
                }
            }
        });
    }

    private void startBicycleAnimation(ImageView bicycle) {
        toggleVisibility(View.VISIBLE);
        animatedBicycle.setAlpha(0.01f);
        animatedBicycle.setTranslationY(BICYCLE_START_BOTTOM);
        loadingText.setAlpha(0.01f);
        loadingText.setTranslationY(20);


        bicycle.animate().alpha(1f).translationY(-BICYCLE_RAISE_TOP)
                .withLayer()
                .setDuration(BICYCLE_RAISE_DURATION)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> {
                    bicycle.animate()
                            .translationY(0)
                            .setDuration((BICYCLE_FALL_DURATION))
                            .setInterpolator(new BounceInterpolator())
                            .withLayer()
                            .withEndAction(() -> {
                                if (bicycleDrawable != null) {
                                    bicycleDrawable.start();
                                }
                            })
                            .start();
                }).start();


        if (dotsAnimator == null) {
            dotsAnimator = ValueAnimator.ofInt(0, 4).setDuration(2000);
            dotsAnimator.setRepeatCount(ValueAnimator.INFINITE);
            dotsAnimator.addListener(new AnimatorLayerListener(loadingText));
            dotsAnimator.addUpdateListener(animation -> {
                int i = (int) animation.getAnimatedValue();
                loadingText.setText(getResources().getString(R.string.loading_trim) + dotsArray[i]);
            });
        }

        loadingText.animate().alpha(1).translationY(0)
                .setDuration(TEXT_SHOW_DURATION)
                .setStartDelay(BICYCLE_FALL_DURATION + BICYCLE_RAISE_DURATION + 300)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(dotsAnimator::start)
                .start();
    }

    private void toggleVisibility(int visibility) {
        if (animatedBicycle != null) {
            animatedBicycle.setVisibility(visibility);
        }

        if (loadingText != null) {
            loadingText.setVisibility(visibility);
        }
    }

    @Override
    public void setPullHeight(float pullHeight) {
        super.setPullHeight(pullHeight);
    }

    @Override
    public void setHeaderHeight(float headerHeight) {
        super.setHeaderHeight(headerHeight);
    }

    @Override
    public boolean isRefreshing() {
        return super.isRefreshing();
    }

    @Override
    public void finishRefreshing() {
        super.finishRefreshing();
        if (bicycleDrawable != null) { bicycleDrawable.stop(); }
        if (animatedBicycle != null) {
            animatedBicycle.setVisibility(GONE);
        }
    }

    public interface OnRefreshListener {

        void onRefresh(YukLoadingLayout yukLoadingLayout);

    }
}
