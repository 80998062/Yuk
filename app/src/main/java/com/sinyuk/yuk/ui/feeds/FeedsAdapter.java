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

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 * 尽量减少这里的逻辑
 */
public class FeedsAdapter extends BaseRVAdapter<Shot> implements Action1<List<Shot>> {
    private final DrawableRequestBuilder<String> avatarBuilder;
    private final DrawableRequestBuilder<String> GIFBuilder;
    private final DrawableRequestBuilder<String> PNGBuilder;
    private boolean isAutoPlayGif = false;

    public FeedsAdapter(Context context, RequestManager requestManager, ArrayList<Shot> dataSet) {
        super(dataSet);
        Timber.tag("FeedsAdapter");
        GIFBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop();
        PNGBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop();
        avatarBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().bitmapTransform(new CropCircleTransformation(context));
    }

    @Override
    protected void footerOnVisibleItem() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateMyItemViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder((FeedItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_list_item, parent, false));
    }

    @Override
    public void onBindMyItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Shot data = getAll().get(position);
        if (data.isAnimated()) {
            ((FeedItemViewHolder) holder).bindTo(data, GIFBuilder, isAutoPlayGif, avatarBuilder);
        } else {
            ((FeedItemViewHolder) holder).bindTo(data, PNGBuilder, isAutoPlayGif, avatarBuilder);
        }
    }


    public void setAutoPlayGif(boolean autoPlayGif) {
        this.isAutoPlayGif = autoPlayGif;
    }

    @Override
    public void call(List<Shot> shots) {
        reset(shots);
        Timber.d("Data in adapter %s", shots.toString());
    }


    public class FeedItemViewHolder extends RecyclerView.ViewHolder {
        private final FeedItemView feedItemView;

        public FeedItemViewHolder(FeedItemView feedItemView) {
            super(feedItemView);
            this.feedItemView = feedItemView;
        }

        public void bindTo(Shot data,
                           DrawableRequestBuilder<String> shotBuilder,
                           boolean isAutoPlayGif,
                           DrawableRequestBuilder<String> avatarBuilder) {
            feedItemView.bindTo(data, shotBuilder, isAutoPlayGif, avatarBuilder);
        }

    }
}
