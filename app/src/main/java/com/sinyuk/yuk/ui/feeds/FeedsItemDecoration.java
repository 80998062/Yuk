package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sinyuk.yuk.utils.ScreenUtils;

/**
 * Created by Sinyuk on 16/8/17.
 */
public class FeedsItemDecoration extends RecyclerView.ItemDecoration {


    private final int marginTop;
    private final int gap;

    public FeedsItemDecoration(Context context) {
        marginTop = ScreenUtils.dpToPxInt(context, 8);
        gap = ScreenUtils.dpToPxInt(context, 16);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position == 0) {
            outRect.top = marginTop;
        }

        outRect.bottom = gap;

    }
}
