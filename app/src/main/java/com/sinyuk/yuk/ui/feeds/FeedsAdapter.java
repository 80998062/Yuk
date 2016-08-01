package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.user.User;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;
import com.sinyuk.yuk.utils.glide.DribbbleTarget;
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


    private final DrawableRequestBuilder<String> avatarRequest;
    private final DrawableRequestBuilder<String> drawableRequestBuilder;

    private int[] stolenSize;
    private Context mContext;
    private ArrayList<Shot> mDataSet = new ArrayList<>();
    private boolean mAutoPlayGif = false;


    public FeedsAdapter(Context context, RequestManager mGlide, ArrayList<Shot> dataSet) {
        Timber.tag("FeedsAdapter");
        this.mContext = context;
        this.drawableRequestBuilder = mGlide
                .fromString()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (e != null) {
                            e.printStackTrace();
                            Timber.e(e.getLocalizedMessage());
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Timber.e("Ready");
                        return false;
                    }
                });


        this.mDataSet = dataSet;

        avatarRequest = mGlide.fromString()
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .crossFade(1000)
                .bitmapTransform(new CropCircleTransformation(mContext));
    }


    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final FeedItemViewHolder holder = new FeedItemViewHolder(View.inflate(mContext, R.layout.feed_list_item, null));
        setOnTouchListener(holder.mShot);
        return holder;
    }

    private void setOnTouchListener(ImageView shot) {
        Timber.e("setOnTouchListener");
        shot.setClickable(true);
        shot.setLongClickable(true);
        shot.setOnLongClickListener(view -> {
            final GifDrawable gif = getGifDrawableIfExisted(shot);
            if (gif != null && gif.isAnimated() && !gif.isRunning()) {
                gif.start();
            }
            return false;
        });
        shot.setOnTouchListener((v, event) -> {
                    final GifDrawable gif = getGifDrawableIfExisted(shot);
                    if (gif == null) {
                        return false;
                    }
                    if (!gif.isAnimated() || !gif.isRunning()) { return false; }
                    if (event.getAction() == MotionEvent.ACTION_UP
                            || event.getAction() == MotionEvent.ACTION_CANCEL) { gif.stop(); }
                    return false;
                }
        );
    }

    private GifDrawable getGifDrawableIfExisted(ImageView shot) {
        // get the image and check if it's an animated GIF
        final Drawable drawable = shot.getDrawable();
        if (drawable == null) { return null; }
        GifDrawable gif = null;
        if (drawable instanceof GifDrawable) {
            gif = (GifDrawable) drawable;
        } else if (drawable instanceof TransitionDrawable) {
            // we fade in images on load which uses a TransitionDrawable; check its layers
            TransitionDrawable fadingIn = (TransitionDrawable) drawable;
            for (int i = 0; i < fadingIn.getNumberOfLayers(); i++) {
                if (fadingIn.getDrawable(i) instanceof GifDrawable) {
                    gif = (GifDrawable) fadingIn.getDrawable(i);
                    break;
                }
            }
        }
        return gif;
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
        Timber.e("I will load %s", data.bestQuality());
        DribbbleTarget target = drawableRequestBuilder
                .load(data.bestQuality())
                .into(new DribbbleTarget(holder.mShot, data.isAnimated(), mAutoPlayGif));
        if (stolenSize == null) {
            // assuming uniform sizing among items (fixed size or match works, wrap doesn't)
            target.getSize((width, height) -> {
                if (0 < width && 0 < height) {
                    stolenSize = new int[]{width, height};
                }
            });
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
        // null-check hell oh shit!!
        final String username = TextUtils.isEmpty(data.getUsername()) ? " " : data.getUsername();
//        final String username = data.getUsername();

        // use a TextDrawable as a placeholder
        final char firstLetter = username.charAt(0);

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(firstLetter + "", mContext.getResources().getColor(R.color.official_slate));

        avatarRequest.load(data.getAvatarUrl())
                .placeholder(textDrawable)
                .error(textDrawable).into(holder.mAvatar);

        /* username */
        checkText(holder.mUsername, username);

        /* type */
        final String type = TextUtils.isEmpty(data.getPlayerOrTeam()) ? "" : data.getPlayerOrTeam();
        switch (type) {
            case User.TEAM:
                checkText(holder.mType, User.TEAM);
                break;
            case User.PLAYER:
                checkText(holder.mType, data.isPro() ? User.PRO : User.PLAYER);
                break;
            default:
                holder.mType.setVisibility(View.GONE);
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
    public GenericRequestBuilder getPreloadRequestBuilder(Shot data) {
        return drawableRequestBuilder.load(data.bestQuality());
    }

    // ------------------------ PreloadSizeProvider -----------------------
    @Override
    public int[] getPreloadSize(Shot item, int adapterPosition, int perItemPosition) {
        return stolenSize;
    }

    public void setAutoPlayGif(boolean autoPlayGif) {
        this.mAutoPlayGif = autoPlayGif;
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
