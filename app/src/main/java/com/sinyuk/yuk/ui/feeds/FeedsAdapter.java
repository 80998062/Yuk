package com.sinyuk.yuk.ui.feeds;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
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
import com.sinyuk.yuk.utils.FormatUtils;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;
import com.sinyuk.yuk.utils.glide.DribbbleTarget;
import com.sinyuk.yuk.utils.glide.ObservableColorMatrix;
import com.sinyuk.yuk.widgets.BadgedFourThreeImageView;
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
    private final DrawableRequestBuilder<String> gifRequestBuilder;
    private final DrawableRequestBuilder<String> drawableRequestBuilder;

    private int[] stolenSize;
    private Context mContext;
    private ArrayList<Shot> mDataSet = new ArrayList<>();
    private boolean mAutoPlayGif = false;


    public FeedsAdapter(Context context, RequestManager mGlide, ArrayList<Shot> dataSet) {
        Timber.tag("FeedsAdapter");
        this.mContext = context;

        this.gifRequestBuilder = mGlide
                .fromString()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop();

        this.drawableRequestBuilder = mGlide
                .fromString()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .dontAnimate()
                .centerCrop();

        this.mDataSet = dataSet;

        avatarRequest = mGlide.fromString()
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .dontAnimate()
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
        // play animated GIFs whilst touched
        shot.setOnTouchListener((v, event) -> {
            // check if it's an event we care about, else bail fast
            final int action = event.getAction();
            if (!(action == MotionEvent.ACTION_DOWN
                    || action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL)) { return false; }

            // get the image and check if it's an animated GIF
            final GifDrawable gif = getGifDrawableIfExisted(shot);
            if (gif == null) {
                return false;
            }
            final int originLayerType = shot.getLayerType();
            // GIF found, start/stop it on press/lift
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    shot.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    gif.start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    gif.stop();
                    shot.setLayerType(originLayerType, null);
                    break;
            }
            return false;
        });
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
        final DrawableRequestBuilder<String> builder =
                data.isAnimated() ? gifRequestBuilder : drawableRequestBuilder;
        DribbbleTarget target = builder
                .load(data.bestQuality())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!data.isHasFadedIn()) {
                            holder.mShot.setHasTransientState(true);
                            final ObservableColorMatrix cm = new ObservableColorMatrix();
                            ObjectAnimator saturation = ObjectAnimator.ofFloat(cm,
                                    ObservableColorMatrix.SATURATION, 0f, 1f);
                            saturation.addUpdateListener(valueAnimator -> {
                                // just animating the color matrix does not invalidate the
                                // drawable so need this update listener.  Also have to create a
                                // new CMCF as the matrix is immutable :(
                                if (holder.mShot.getDrawable() != null) {
                                    holder.mShot.getDrawable().setColorFilter(
                                            new ColorMatrixColorFilter(cm));
                                }
                            });
                            saturation.setDuration(2000);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                saturation.setInterpolator(AnimationUtils.loadInterpolator(mContext,
                                        android.R.interpolator.fast_out_slow_in));
                            }
                            saturation.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    holder.mShot.setHasTransientState(false);
                                }
                            });
                            saturation.start();
                            data.setHasFadedIn(true);
                        }
                        return false;
                    }
                })
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
        checkText(holder.mViewCount, FormatUtils.shortenNumber(data.getViewsCount() + ""));

        /* comments */

        checkText(holder.mComment, FormatUtils.shortenNumber(data.getCommentsCount() + ""));

        /* like */
        checkText(holder.mLikeNumber, FormatUtils.shortenNumber(data.getLikesCount() + ""));

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
        Timber.e("Type is %s", type);
        switch (type) {
            case User.TEAM:
                checkText(holder.mType, User.TEAM);
                holder.mType.setVisibility(View.VISIBLE);
                break;
            case User.PLAYER:
            default:
                checkText(holder.mType, data.isPro() ? User.PRO : User.PLAYER);
                if (data.isPro()) {holder.mType.setVisibility(View.VISIBLE);}
                break;
        }

    }

    private void checkText(TextView textView, String text) {
        if (textView != null && !TextUtils.isEmpty(text)) {textView.setText(text);}
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
        return gifRequestBuilder.load(data.bestQuality());
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
        BadgedFourThreeImageView mShot;
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
            ButterKnife.bind(this, itemView);

        }


    }
}
