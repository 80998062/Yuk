package com.sinyuk.yuk.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.utils.anim.EaseSineInInterpolator;
import com.sinyuk.yuk.utils.anim.EaseSineOutInterpolator;

/**
 * Created by Sinyuk on 16/7/11.
 */
public class DribbleHeart extends View implements SpringListener {
    private static float scaleFactor = 1.09f;
    private static long bounceDuration = 150;

    private float totalOffset; // 球的半径
    private RectF groundRect = new RectF();
    private Rect ballRect = new Rect();
    private Paint groundPaint;
    private int topPadding;
    private int bottomPadding;
    private float groundHeight;
    private float groundWidth; // 地面宽度
    private float ballRadius;
    private Drawable ballDrawable;
    private Spring spring;
    private ValueAnimator accelerateFall;
    private long interval;
    private ValueAnimator decelerateRaise;
    private boolean isUp;
    private int centerX;
    private float scaleX = 1;
    private float scaleY = 1;
    private boolean isStartFromTop;
    private float transitionY;
    private int mWidth;
    private int mHeight;
    private boolean isCancelByMe;


    public DribbleHeart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialAttributes(context, attrs);
    }

    public DribbleHeart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        initialAttributes(context, attrs);
    }

    private void initialAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DribbleHeart);
        try {
            ballRadius = a.getDimensionPixelOffset(R.styleable.DribbleHeart_ball_radius, 0);
            topPadding = a.getDimensionPixelOffset(R.styleable.DribbleHeart_top_padding, 4);
            bottomPadding = a.getDimensionPixelOffset(R.styleable.DribbleHeart_bottom_padding, 4);
            groundHeight = a.getDimensionPixelOffset(R.styleable.DribbleHeart_ground_height, 0);
            groundWidth = a.getDimensionPixelOffset(R.styleable.DribbleHeart_ground_width, 0);
            isStartFromTop = a.getBoolean(R.styleable.DribbleHeart_is_start_from_top, false);
            interval = (long) a.getInt(R.styleable.DribbleHeart_interval, 500);
            // 下落的时间不能小于
            interval = Math.max(interval, 2 * bounceDuration);
            ballDrawable = getResources().getDrawable(R.drawable.dribbble_ball);
            if (ballDrawable == null) { throw new NullPointerException("Must have dribble logo"); }
        } finally {
            a.recycle();
        }

        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        // 卡的一逼
//        groundPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        reset(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGround(canvas);
        drawBall(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createSpring();
        createPaint();
        createAnim();
    }

    private void reset(int w, int h) {
        scaleX = scaleY = 1;

        centerX = w / 2;

        // 计算球和地面的距离
        if (ballRadius == 0) {
            ballRadius = centerX / 1.5f;
        }
        //计算底部相关
        if (isStartFromTop) {
            groundHeight = ballRadius * 0.3f;
            groundWidth = ballRadius * .8f;
        } else {
            groundHeight = ballRadius * 0.3f - ballRadius * 0.1f;
            groundWidth = ballRadius * .8f + ballRadius * 0.6f;
        }
        totalOffset = h - topPadding - bottomPadding - groundHeight * 0.3f - ballRadius * 2;

        transitionY = isStartFromTop ? 0 : totalOffset;

        updateGroundRect();
        updateBallRect();
    }

    private void createPaint() {
        groundPaint = new Paint();
        groundPaint.setColor(getResources().getColor(R.color.official_slate));
        groundPaint.setAntiAlias(true);
        isUp = !isStartFromTop;
        final int alpha = isStartFromTop ? 75 : 200;
        groundPaint.setAlpha(alpha);
    }

    private void updateGroundRect() {
        float groundLeft = (centerX - groundWidth / 2);
        float groundRight = (centerX + groundWidth / 2);
        float groundBottom = getBottom() - bottomPadding;
        float groundTop = groundBottom - groundHeight;
        groundRect.set(groundLeft, groundTop, groundRight, groundBottom);
    }

    private void updateBallRect() {
        int ballLeft = (int) (centerX - ballRadius * scaleX);
        int ballRight = (int) (centerX + ballRadius * scaleX);
        int ballBottom = (int) (getTop() + transitionY + topPadding + 2 * ballRadius);
        int ballTop = (int) (ballBottom - 2 * ballRadius * scaleY);
        ballRect.set(ballLeft, ballTop, ballRight, ballBottom);
    }

    private void drawBall(Canvas canvas) {
        ballDrawable.setBounds(ballRect);
        ballDrawable.draw(canvas);
    }

    private void drawGround(Canvas canvas) {
        canvas.drawOval(groundRect, groundPaint);
    }

    public void dribble() {
        resetSpring();
        if (accelerateFall == null || decelerateRaise == null) {
            return;
        }
        if (isStartFromTop) {
            accelerateFall.start();
        } else {
            decelerateRaise.start();
        }
    }

    public void stop() {

        if (accelerateFall != null && accelerateFall.isRunning()) {
            accelerateFall.cancel();
        }
        if (decelerateRaise != null && decelerateRaise.isRunning()) {
            decelerateRaise.cancel();
        }
        if (null != spring) {
            spring.removeListener(this);
        }
        reset(mWidth, mHeight);
        invalidate();
    }

    private void createSpring() {
        SpringSystem springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
        spring.setOvershootClampingEnabled(true);
    }

    private void resetSpring() {
        if (spring != null) {
            final float initValue = isStartFromTop ? 1 : 0;
            spring.setCurrentValue(initValue).setAtRest();
            spring.addListener(this);
        }
    }

    private void createAnim() {
        final long squeezeDelay = interval - bounceDuration;
        // 第一阶段 加速下落
        accelerateFall = ValueAnimator.ofFloat(0, 1)
                .setDuration(interval);
        accelerateFall.setInterpolator(new EaseSineInInterpolator());
        decelerateRaise = ValueAnimator.ofFloat(0, 1)
                .setDuration(interval);
        decelerateRaise.setInterpolator(new EaseSineOutInterpolator());

        accelerateFall.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                postDelayed(() -> squeeze(), squeezeDelay);
            }
        });
        decelerateRaise.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {isCancelByMe = true;}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCancelByMe) {
                    accelerateFall.start();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isCancelByMe = false;
                recover();
            }
        });

        accelerateFall.addUpdateListener(animator ->
        {
            float fraction = animator.getAnimatedFraction();
            transitionY = totalOffset * fraction;
            groundHeight = ballRadius * 0.3f - ballRadius * 0.1f * fraction * fraction;
            groundWidth = ballRadius * .8f + ballRadius * 0.6f * fraction * fraction;
            groundPaint.setAlpha((int) (75 + 125 * fraction * fraction));
            updateBallRect();
            updateGroundRect();
            invalidateRect();
        });
        decelerateRaise.addUpdateListener(animator ->
        {
            float fraction = animator.getAnimatedFraction();
            transitionY = totalOffset * (1 - fraction);
            groundPaint.setAlpha((int) (200 - 125 * fraction * fraction));
            groundHeight = ballRadius * 0.2f + ballRadius * 0.1f * fraction * fraction;
            groundWidth = ballRadius * 1.4f - ballRadius * 0.6f * fraction * fraction;
            updateBallRect();
            updateGroundRect();
            invalidateRect();
        });

    }

    private void invalidateRect() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) //is main thread
        {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private void squeeze() {
        SpringConfig config = SpringConfig.fromBouncinessAndSpeed(2.8f, 0.2);
        spring.setSpringConfig(config);
        spring.setEndValue(0);
    }


    private void recover() {
        SpringConfig config = SpringConfig.fromBouncinessAndSpeed(3f, 0.22);
        spring.setSpringConfig(config);
        spring.setVelocity(ballRadius * 0.15 / bounceDuration);
        spring.setEndValue(1);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        if (isUp) {
            scaleX = (float) SpringUtil.mapValueFromRangeToRange(
                    spring.getCurrentValue(), 0, 1, scaleFactor, 1);
            scaleY = (float) SpringUtil.mapValueFromRangeToRange(
                    spring.getCurrentValue(), 0, 1, 1 / scaleFactor, 1f);
        } else {
            scaleX = (float) SpringUtil.mapValueFromRangeToRange(
                    spring.getCurrentValue(), 1, 0, 1, scaleFactor);
            scaleY = (float) SpringUtil.mapValueFromRangeToRange(
                    spring.getCurrentValue(), 1, 0, 1, 1 / scaleFactor);
        }

    }

    @Override
    public void onSpringAtRest(Spring spring) {
        if (isUp) {
            decelerateRaise.start();
        }
    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {
        isUp = !isUp;
    }


}
