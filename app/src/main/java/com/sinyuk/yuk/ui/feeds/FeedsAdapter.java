package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.BaseRVAdapter;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 * 尽量减少这里的逻辑
 */
public class FeedsAdapter extends BaseRVAdapter<Shot, FeedsAdapter.FeedItemViewHolder> {
    private final DrawableRequestBuilder<String> avatarBuilder;
    private final DrawableRequestBuilder<String> GIFBuilder;
    private final DrawableRequestBuilder<String> PNGBuilder;
    private boolean isAutoPlayGif = false;

    public FeedsAdapter(Context context, RequestManager requestManager) {
        Timber.tag("FeedsAdapter");
        GIFBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop();
        PNGBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop();
        avatarBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.RESULT).dontAnimate().centerCrop().bitmapTransform(new CropCircleTransformation(context));
    }

    @Override
    public FeedItemViewHolder onCreateMyItemViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder((FeedItemView) LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false));
    }

    @Override
    public void onBindMyItemViewHolder(FeedItemViewHolder holder, int position) {
        final Shot data = getDataSet().get(position);
        if (data.isAnimated()) {
            holder.bindTo(data, GIFBuilder, isAutoPlayGif, avatarBuilder);
        } else {
            holder.bindTo(data, PNGBuilder, isAutoPlayGif, avatarBuilder);
        }
    }


    public void setAutoPlayGif(boolean autoPlayGif) {
        this.isAutoPlayGif = autoPlayGif;
    }

    public class FeedItemViewHolder extends RecyclerView.ViewHolder {
        private final FeedItemView feedItemView;

        public FeedItemViewHolder(FeedItemView feedItemView) {
            super(feedItemView);
            this.feedItemView = feedItemView;
        }

        public void bindTo(Shot data, DrawableRequestBuilder<String> shotBuilder, boolean isAutoPlayGif, DrawableRequestBuilder<String> avatarBuilder) {
            feedItemView.bindTo(data, shotBuilder, isAutoPlayGif, avatarBuilder);
        }

    }
}
