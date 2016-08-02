package com.sinyuk.yukloadinglayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by Sinyuk on 16/8/1.
 */
public class AnimatorLayerListener extends AnimatorListenerAdapter {

    private View mView;
    private int mOriginLayerType;

    public AnimatorLayerListener(View v) {
        mView = v;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mOriginLayerType = mView.getLayerType();
        mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mView.setLayerType(mOriginLayerType, null);
    }

}
