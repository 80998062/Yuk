package com.sinyuk.yuk.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sinyuk.yuk.R;
import com.sinyuk.yuk.widgets.DribbleHeart;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16/8/17.
 */
public class LoadingLayout extends RelativeLayout {

    DribbleHeart mDribbleLoadingView;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void ensureLoadingView() {
        mDribbleLoadingView = (DribbleHeart) findViewById(R.id.dribble_loading_view);
        if (mDribbleLoadingView == null) {
            throw new NullPointerException("dribble loading view is Null");
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (View.VISIBLE != visibility) {
            if (mDribbleLoadingView != null) {
                mDribbleLoadingView.stop();
                Timber.d("DribbleLoadingView Stop");
            }
        } else {
            if (mDribbleLoadingView != null) {
                mDribbleLoadingView.dribble();
                Timber.d("DribbleLoadingView Start");
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureLoadingView();
//        postDelayed(() -> mDribbleLoadingView.dribble(),500);
    }
}
