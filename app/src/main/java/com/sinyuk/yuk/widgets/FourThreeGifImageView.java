package com.sinyuk.yuk.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Sinyuk on 16/9/10.
 */
public class FourThreeGifImageView extends GifImageView {
    public FourThreeGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int fourThreeHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthSpec) * 3 / 4,
                View.MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, fourThreeHeight);
    }
}
