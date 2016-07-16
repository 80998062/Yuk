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
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.user.User;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;
import com.sinyuk.yuk.widgets.FourThreeImageView;
import com.sinyuk.yuk.widgets.NumberTextView;
import com.sinyuk.yuk.widgets.TextDrawable;

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
    private final DrawableRequestBuilder<String> avatarRequest;
    private final RequestManager mGlide;

    private int[] stolenSize;
    private Context mContext;
    private ArrayList<Shot> mDataSet = new ArrayList<>();

    public FeedsAdapter(Context context, RequestManager mGlide, ArrayList<Shot> dataSet) {
        this.mContext = context;
        this.mGlide = mGlide;
        this.builder = mGlide.fromString().asBitmap().centerCrop();
        this.mDataSet = dataSet;
        Timber.tag("FeedsAdapter");

        avatarRequest = mGlide.fromString()
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(mContext));
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
        /* shot */
        if (data.getImages() == null) {
            // cache
            mGlide.load(R.drawable.pic_fill).into(holder.mShot);
        } else {
            Target<?> target = builder.load(data.getImages().getNormal())
                    .error(R.drawable.pic_fill)
                    .into(holder.mShot);
            if (stolenSize == null) {
                // assuming uniform sizing among items (fixed size or match works, wrap doesn't)
                target.getSize((width, height) -> {
                    if (0 < width && 0 < height) {
                        stolenSize = new int[]{width, height};
                    }
                });
            }
        }
        /* rebound */
        if (data.getReboundsCount() > 0) {
            if (holder.mReboundStub != null) {
                holder.mReboundStub.inflate();
                holder.mReboundStub = null;
            }
            // TODO: go to url
        }
        /* attachment */
        if (data.getAttachmentsCount() > 0) {
            if (holder.mAttachmentStub != null) {
                holder.mAttachmentStub.inflate();
                holder.mAttachmentStub = null;
            }

            // TODO: go to url

        }
        /* view count */
        checkText(holder.mViewCount, data.getViewsCount() + "");

        /* comments */

        checkText(holder.mComment, data.getCommentsCount() + "");

        /* like */
        checkText(holder.mLikeNumber, data.getLikesCount() + "");

        // TODO: 是否已经收藏


        /* avatar*/
        final String username = data.getUser().getUsername();

        // use a TextDrawable as a placeholder
        final char firstLetter = username.isEmpty() ? ' ' : username.charAt(0);

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(firstLetter + "", mContext.getResources().getColor(R.color.official_slate));

        avatarRequest.load(data.getUser().getAvatarUrl())
                .placeholder(textDrawable)
                .error(textDrawable).into(holder.mAvatar);

        /* username */
        checkText(holder.mUsername, username);

        /* type */
        switch (data.getUser().getType()) {
            case User.TEAM:
                checkText(holder.mType, User.TEAM);
                break;
            case User.PLAYER:
                checkText(holder.mType, data.getUser().isPro() ? User.PRO : User.PLAYER);
                break;
            default:
                break;
        }

    }

    private void checkText(TextView textView, String text) {
        if (textView != null && text != null) {textView.setText(text);}
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
        FourThreeImageView mShot;
        @BindView(R.id.rebound_stub)
        ViewStub mReboundStub;
        @BindView(R.id.attachment_stub)
        ViewStub mAttachmentStub;
        @BindView(R.id.view_count)
        NumberTextView mViewCount;
        @BindView(R.id.comment)
        NumberTextView mComment;
        @BindView(R.id.like_icon)
        ImageView mLikeIcon;
        @BindView(R.id.like_number)
        NumberTextView mLikeNumber;
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
            ButterKnife.bind(this, itemView);
        }
    }
}
