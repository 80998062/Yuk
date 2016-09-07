package com.sinyuk.yuk.ui.feeds;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.user.User;
import com.sinyuk.yuk.ui.detail.DetailActivity;
import com.sinyuk.yuk.utils.FormatUtils;
import com.sinyuk.yuk.utils.Preconditions;
import com.sinyuk.yuk.utils.StringUtils;
import com.sinyuk.yuk.utils.anim.EaseSineOutInterpolator;
import com.sinyuk.yuk.utils.glide.DribbbleTarget;
import com.sinyuk.yuk.utils.glide.ObservableColorMatrix;
import com.sinyuk.yuk.widgets.BadgedFourThreeImageView;
import com.sinyuk.yuk.widgets.TextDrawable;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sinyuk on 16/8/10.
 */
public class FeedItemView extends RelativeLayout {
    private final Interpolator INTERPOLATOR;
    @BindColor(R.color.official_slate)
    int COLOR_SLATE;
    @BindView(R.id.shot)
    BadgedFourThreeImageView mShot;
    @BindView(R.id.rebound_stub)
    ViewStub mReboundStub;
    @BindView(R.id.attachment_stub)
    ViewStub mAttachmentStub;
    @BindView(R.id.comment_btn)
    ImageView mCommentBtn;
    @BindView(R.id.like_btn)
    ImageView mLikeBtn;
    @BindView(R.id.card_view)
    CardView mCardView;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.type)
    TextView mType;


    public FeedItemView(Context context) {
        this(context, null);
    }

    public FeedItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            INTERPOLATOR = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
        } else {
            INTERPOLATOR = new EaseSineOutInterpolator();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(Shot data,
                       DrawableRequestBuilder<String> shotBuilder,
                       boolean isAutoPlayGif,
                       DrawableRequestBuilder<String> avatarBuilder) {
        Preconditions.checkNotNull(mShot);

        /*加载图片*/
        shotBuilder.load(data.bestQuality())
                /*.listener(new ShotRequestListener(data))*/
                .into(new DribbbleTarget(mShot, isAutoPlayGif));

        /* rebound */
        if (data.getReboundsCount() > 0) {
            if (mReboundStub != null) {
                mReboundStub.inflate();
                mReboundStub = null;
            }
            // TODO: set onClick listener
        }
        /* attachment */
        if (data.getAttachmentsCount() > 0) {
            if (mAttachmentStub != null) {
                mAttachmentStub.inflate();
                mAttachmentStub = null;
            }
            // TODO: set onClick listener
        }

//        /* view count */
//        setText(mViewCount, getNumberToString(data.getViewsCount()));
//
//        /* comments */
//
//        setText(mComment, getNumberToString(data.getCommentsCount()));
//
//        /* like */        // TODO: 是否已经收藏
//        setText(mLikeNumber, getNumberToString(data.getLikesCount()));

        final User user = data.getUser();

        Preconditions.checkNotNull(user, "Can't bind to a null user");
         /* avatar*/
        final String username = StringUtils.valueOrDefault(user.getUsername(), " ");

        // use a TextDrawable as a placeholder
        final char firstLetter = username.charAt(0);

        final TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(firstLetter + "", COLOR_SLATE);

        avatarBuilder.load(user.getAvatarUrl())
                .placeholder(textDrawable)
                .error(textDrawable)
                .into(mAvatar);

        /*username*/
        setText(mUsername, username);

        /* type */
        final String type = StringUtils.valueOrDefault(user.getType(), User.PLAYER);

        if (user.isPro() || User.TEAM.equals(type)) {
            mType.setVisibility(View.VISIBLE);
        } else if (User.PLAYER.equals(type)) {
            mType.setVisibility(View.GONE);
        }

        if (user.isPro()) {
            setText(mType, User.PRO);
        } else {
            setText(mType, type);
        }



      /*  mShot.setOnTouchListener((view, motionEvent) -> {
            // check if it's an event we care about, else bail fast
            final int action = motionEvent.getAction();
            if (!(action == MotionEvent.ACTION_DOWN
                    || action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL)) { return false; }

            // get the image and check if it's an animated GIF
            final GifDrawable gif = getGifDrawableIfExisted(mShot);
            if (gif == null) {
                return false;
            }
            final int originLayerType = mShot.getLayerType();
            // GIF found, start/stop it on press/lift
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mShot.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    gif.start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    gif.stop();
                    mShot.setLayerType(originLayerType, null);
                    break;
            }
            return false;
        });*/

        mCardView.setOnClickListener(view -> {
            if (getContext() != null) {
                // create the transition animation - the images in the layouts
                // of both activities are defined with android:transitionName="robot"
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((Activity) getContext(), mCardView, getContext().getString(R.string.transition_shot_root));
                // start the new activity
                getContext().startActivity(DetailActivity.getStartIntent(data, getContext()), options.toBundle());
            }

        });
    }


    /**
     * set text for a text view
     *
     * @param v
     * @param text
     */
    private void setText(TextView v, String text) {
        Preconditions.checkNotNull(v);
        Preconditions.checkNotNull(text);
        v.setText(text);
    }

    private String getNumberToString(int number) {
        return FormatUtils.shortenNumber(String.valueOf(number));
    }

    /**
     * 如果imageView里面是GIF的话
     *
     * @param shot
     * @return
     */
    private GifDrawable getGifDrawableIfExisted(ImageView shot) {
        // get the image and check if it's an animated GIF
        final Drawable drawable = shot.getDrawable();
        if (drawable == null) {
            return null;
        }
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

    @Deprecated
    private class ShotRequestListener implements RequestListener<String, GlideDrawable> {

        private final Shot data;

        public ShotRequestListener(Shot data) {
            this.data = data;
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Preconditions.checkNotNull(mShot, "ImageView Shot Can't be Null");
            if (!data.isHasFadedIn()) {
                mShot.setHasTransientState(true);
                final ObservableColorMatrix cm = new ObservableColorMatrix();
                ObjectAnimator saturation = ObjectAnimator.ofFloat(cm,
                        ObservableColorMatrix.SATURATION, 0f, 1f);
                saturation.addUpdateListener(valueAnimator -> {
                    // just animating the color matrix does not invalidate the
                    // drawable so need this update listener.  Also have to create a
                    // new CMCF as the matrix is immutable :(
                    if (mShot.getDrawable() != null) {
                        mShot.getDrawable().setColorFilter(
                                new ColorMatrixColorFilter(cm));
                    }
                });
                saturation.setDuration(1600);
                saturation.setInterpolator(INTERPOLATOR);
                saturation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mShot.setHasTransientState(false);
                    }
                });
                saturation.start();
                data.setHasFadedIn(true);
            }
            return false;
        }
    }
}
