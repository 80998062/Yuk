package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.shot.ShotDiffCallback;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 * 尽量减少这里的逻辑
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedItemViewHolder>
    /*    implements ListUpdateCallback*/ {
    private final static int CROSS_FADE_DURATION = 1500;
    private final DrawableRequestBuilder<String> avatarBuilder;
    private final DrawableRequestBuilder<String> GIFBuilder;
    private final DrawableRequestBuilder<String> PNGBuilder;
    private boolean isAutoPlayGif = false;
    private List<Shot> mDataSet = new ArrayList<>();

    public FeedsAdapter(Context context, RequestManager requestManager) {
        Timber.tag("FeedsAdapter");
        GIFBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade(CROSS_FADE_DURATION).centerCrop();
        PNGBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.RESULT).crossFade(CROSS_FADE_DURATION).centerCrop();
        avatarBuilder = requestManager.fromString().diskCacheStrategy(DiskCacheStrategy.RESULT).dontAnimate().centerCrop().bitmapTransform(new CropCircleTransformation(context));
    }

    // 每次传递进来全部的items
//    public void addOrUpdate(List<Shot> data) {
//        Timber.d("New data: %s size %d", data.toString(), data.size());
//        Timber.d("Old data: %s size %d", mDataSet.toString(), mDataSet.size());
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ShotDiffCallback(mDataSet, data), false);
//        this.mDataSet.clear();
//        this.mDataSet.addAll(data);
//        diffResult.dispatchUpdatesTo((ListUpdateCallback) this);
//    }

    public void appendAll(List<Shot> items) {
        int startPosition = mDataSet.size();
        mDataSet.addAll(items);
        notifyItemRangeInserted(startPosition,items.size());
    }

    public void addAll(List<Shot> items) {
        mDataSet.clear();
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void setAutoPlayGif(boolean autoPlayGif) {
        this.isAutoPlayGif = autoPlayGif;
    }

    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder((FeedItemView) LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder holder, int position) {
        // no-op
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder holder, int position, List<Object> payloads) {
        final Shot data = mDataSet.get(position);
        if (data.isAnimated()) {
            holder.bindTo(data, GIFBuilder, isAutoPlayGif, avatarBuilder);
        } else {
            holder.bindTo(data, PNGBuilder, isAutoPlayGif, avatarBuilder);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public List<Shot> getData() {
        return mDataSet;
    }



 /*   @Override
    public void onInserted(int position, int count) {
        Timber.d("Data count onInserted : %d", mDataSet.size());
        Timber.d("Insert position: %d , count: %d", position, count);
        for (int i = position; i < position + count; i++) {
            notifyItemInserted(i);
            Timber.d("append: %d ", i);
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        for (int i = position; i < position + count; i++) {
            notifyItemRemoved(i);
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        Timber.d("Data count onChanged : %d", mDataSet.size());
        Timber.d("Changed position: %d , count: %d", position, count);
        notifyItemRangeChanged(position, count, payload);
    }*/

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
