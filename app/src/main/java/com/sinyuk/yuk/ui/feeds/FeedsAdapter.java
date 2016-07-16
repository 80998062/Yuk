package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.widgets.RatioImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedItemViewHolder> implements ListPreloader.PreloadModelProvider<Shot>, ListPreloader.PreloadSizeProvider<Shot> {


    private final BitmapRequestBuilder<String, Bitmap> builder;
    private int[] stolenSize;
    private Context mContext;
    private ArrayList<Shot> mDataSet = new ArrayList<>();

    public FeedsAdapter(Context context, RequestManager mGlide, ArrayList<Shot> dataSet) {
        this.mContext = context;
        this.builder = mGlide.fromString().asBitmap().centerCrop();
        this.mDataSet = dataSet;
        Timber.tag("FeedsAdapter");
    }


    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder(View.inflate(mContext, R.layout.feed_list_item, null));
    }


    // ------------------------ RecyclerView.Adapter -----------------------
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder holder, int position) {
        final Shot data = mDataSet.get(position);
        Target<?> target = builder.load(data.getImages().getNormal()).into(holder.mShot);
        if (stolenSize == null) {
            // assuming uniform sizing among items (fixed size or match works, wrap doesn't)
            target.getSize((width, height) -> {
                if (0 < width && 0 < height) {
                    stolenSize = new int[]{width, height};
                }
            });
        }
    }

    // ------------------------ PreloadModelProvider -----------------------

    public Shot getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public List<Shot> getPreloadItems(int position) {
        return Collections.singletonList(getItem(position));
    }

    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(Shot item) {
        return builder.load(item.getImages().getNormal());
    }

    // ------------------------ PreloadSizeProvider -----------------------
    @Override
    public int[] getPreloadSize(Shot item, int adapterPosition, int perItemPosition) {
        return stolenSize;
    }

    public class FeedItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shot)
        RatioImageView mShot;
        @BindView(R.id.rebound_stub)
        ViewStub mReboundStub;
        @BindView(R.id.attachment_stub)
        ViewStub mAttachmentStub;
        @BindView(R.id.view_count)
        TextView mViewCount;
        @BindView(R.id.comment)
        TextView mComment;
        @BindView(R.id.like_icon)
        ImageView mLikeIcon;
        @BindView(R.id.like_number)
        TextView mLikeNumber;
        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.avatar)
        ImageView mAvatar;
        @BindView(R.id.username)
        TextView mUsername;
        @BindView(R.id.type)
        TextView mType;

        public FeedItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
