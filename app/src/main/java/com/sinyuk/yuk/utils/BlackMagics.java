package com.sinyuk.yuk.utils;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.ViewPropertyAnimator;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Sinyuk on 16/8/15.
 */
public class BlackMagics {
    /*
     * 进度条上移
     * @param v
     * @param withLayer
     */
    public static ViewPropertyAnimator scrollUp(SmoothProgressBar progressBar) {
        final int offset = progressBar.getHeight();
        progressBar.setTranslationY(offset);
        progressBar.setAlpha(0.01f);
        return progressBar.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withLayer();
    }

    /*
    * 进度条下移
    * @param v
    * @param withLayer
    */
    public static ViewPropertyAnimator scrollDown(SmoothProgressBar progressBar) {
        final int offset = progressBar.getHeight();
        return progressBar.animate()
                .translationY(offset)
                .alpha(0.01f)
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withLayer();
    }
}
