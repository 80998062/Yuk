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
        Timber.d("onVisibilityChanged");
        Timber.d("changedView %s visibility %d ", changedView.getClass().getSimpleName(), visibility);
        if (changedView != this) { return; }

        mDribbleLoadingView.setVisibility(visibility);

        if (View.VISIBLE != visibility) {
            if (mDribbleLoadingView != null) {
                mDribbleLoadingView.stop();
            }
        } else {
            if (mDribbleLoadingView != null) {
                postDelayed(() -> mDribbleLoadingView.dribble(), 100);
            }
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureLoadingView();
    }
}
