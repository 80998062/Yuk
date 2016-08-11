package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedItemViewHolder> {
    private final DrawableRequestBuilder<String> avatarBuilder;
    private final DrawableRequestBuilder<String> shotBuilder;
    private final DrawableRequestBuilder<String> PNGBuilder;

    private Context mContext;
    private ArrayList<Shot> mDataSet = new ArrayList<>();
    private boolean mAutoPlayGif = false;


    public FeedsAdapter(Context context, RequestManager requestManager, ArrayList<Shot> dataSet) {
        Timber.tag("FeedsAdapter");
        this.mContext = context;

        this.shotBuilder = requestManager
                .fromString()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop();

        this.PNGBuilder = requestManager
                .fromString()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .dontAnimate()
                .centerCrop();

        this.mDataSet = dataSet;

        avatarBuilder = requestManager.fromString()
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(mContext));
    }


    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder((FeedItemView) View.inflate(mContext, R.layout.feed_list_item, null));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder holder, int position) {
        final Shot data = mDataSet.get(position);
        if (data.isAnimated()) {
            holder.bindTo(data, shotBuilder, mAutoPlayGif, avatarBuilder);
        }else {
            holder.bindTo(data, PNGBuilder, mAutoPlayGif, avatarBuilder);
        }
    }



    public void setAutoPlayGif(boolean autoPlayGif) {
        this.mAutoPlayGif = autoPlayGif;
    }


    public class FeedItemViewHolder extends RecyclerView.ViewHolder {
        private final FeedItemView itemView;

        public FeedItemViewHolder(FeedItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bindTo(Shot data,
                           DrawableRequestBuilder<String> shotBuilder,
                           boolean isAutoPlayGif,
                           DrawableRequestBuilder<String> avatarBuilder) {
            itemView.bindTo(data, shotBuilder, isAutoPlayGif, avatarBuilder);
        }

    }
}
