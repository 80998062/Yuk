package com.sinyuk.yuk.ui.detail;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jakewharton.rxbinding.support.design.widget.RxAppBarLayout;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.utils.ColorUtils;
import com.sinyuk.yuk.utils.MathUtils;
import com.sinyuk.yuk.utils.Preconditions;
import com.sinyuk.yuk.utils.StringUtils;
import com.sinyuk.yuk.utils.ViewUtils;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;
import com.sinyuk.yuk.utils.glide.GlideUtils;
import com.sinyuk.yuk.utils.spanbuilder.AndroidSpan;
import com.sinyuk.yuk.widgets.FontTextView;
import com.sinyuk.yuk.widgets.FourThreeImageView;
import com.sinyuk.yuk.widgets.ReadMoreTextView;
import com.sinyuk.yuk.widgets.TextDrawable;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/8/20.
 */
public class DetailActivity extends BaseActivity {
    public static final String TAG = "DetailActivity";
    private static final String SHOT_DATA = "shot_data";
    private static final int CROSS_FADE_DURATION = 1000;
    @BindView(R.id.shot)
    FourThreeImageView mShot;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.publish_info)
    TextView mPublishInfo;
    @BindColor(R.color.official_slate)
    int COLOR_SLATE;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.back_btn)
    ImageView mBackBtn;
    @BindView(R.id.like_btn)
    ImageView mLikeBtn;
    @BindView(R.id.likes_tv)
    FontTextView mLikesTv;
    @BindView(R.id.view_btn)
    ImageView mViewBtn;
    @BindView(R.id.views_tv)
    FontTextView mViewsTv;
    @BindView(R.id.bucket_btn)
    ImageView mBucketBtn;
    @BindView(R.id.buckets_tv)
    FontTextView mBucketsTv;
    @BindView(R.id.share_btn)
    ImageView mShareBtn;
    @BindView(R.id.shares_tv)
    FontTextView mSharesTv;
    @BindView(R.id.description_tv)
    ReadMoreTextView mDescriptionTv;
    @BindView(R.id.attachments_container)
    FrameLayout mAttachmentsContainer;
    private Shot mData;

    public static Intent getStartIntent(Shot data, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHOT_DATA, data);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.detail_activity;
    }

    @Override
    protected void beforeInflating() {
        mData = getIntent().getExtras().getParcelable(SHOT_DATA);
        Timber.tag(TAG);
    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {
        setAppBarLayout();
        setupToolbar();
        loadShot();
        loadAuthorInformation();
        loadSocialInformation();
        loadAttachments();
    }

    private void loadAttachments() {
        if (mData.getAttachmentsCount() > 0) {
            mAttachmentsContainer.setVisibility(View.VISIBLE);
        }
    }


    private void setupToolbar() {
    }

    private void setAppBarLayout() {
        addSubscription(RxAppBarLayout.offsetChanges(mAppBarLayout)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(dy -> (dy / (mAppBarLayout.getTotalScrollRange() * 1.f) * (dy / (mAppBarLayout.getTotalScrollRange() * 1.f))))
                .map(fraction -> MathUtils.constrain(0, 1, fraction))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fraction -> {
                    mBackBtn.setAlpha(fraction);
                    mLikeBtn.setAlpha(fraction);
                    if (fraction == 1) {
                        mLikeBtn.setClickable(true);
                        mBackBtn.setClickable(true);
                    } else if (fraction == 0) {
                        mLikeBtn.setClickable(false);
                        mBackBtn.setClickable(false);
                    }
                }));
    }

    // 作者信息
    private void loadAuthorInformation() {
        final String username = StringUtils.valueOrDefault(mData.getUser().getUsername(), " ");
        // use a TextDrawable as a placeholder
        final char firstLetter = username.charAt(0);

        final TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(firstLetter + "", COLOR_SLATE);

        Glide.with(this).load(mData.getUser().getAvatarUrl()).bitmapTransform(new CropCircleTransformation(this)).placeholder(textDrawable).error(textDrawable).into(mAvatar);

        /*username By XXX*/
        setText(mPublishInfo, new AndroidSpan().drawRelativeSize(getString(R.string.by), 1f).drawTextAppearanceSpan(username, this, R.style.sd_username).getSpanText());
        //title
        setText(mTitle, StringUtils.valueOrDefault(mData.getTitle(), ""));

        if (mData.getDescription() != null) {
            mDescriptionTv.setText(mData.getDescription());
        }

    }

    private void loadSocialInformation() {
    }

    // 加载图片
    private void loadShot() {
        DrawableRequestBuilder<String> requestBuilder = Glide.with(this).fromString().crossFade(CROSS_FADE_DURATION).centerCrop();
        if (mData.isAnimated()) {
            requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        } else {
            requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
        }
        requestBuilder.load(mData.bestQuality())
                .error(R.color.colorPrimary)
                .placeholder(R.color.white)
                .listener(new ShotRequestListener(mShot)).into(mShot);
    }

    private void setText(TextView v, String text) {
        Preconditions.checkNotNull(v);
        Preconditions.checkNotNull(text);
        v.setText(text);
    }

    private void setText(TextView v, SpannableStringBuilder text) {
        Preconditions.checkNotNull(v);
        Preconditions.checkNotNull(text);
        v.setText(text);
    }

    @OnClick(R.id.back_btn)
    public void onClickBackButton() {
        onBackPressed();
    }

    @OnClick(R.id.like_btn)
    public void onClickLikeBtn() {

    }

    private class ShotRequestListener implements RequestListener<String, GlideDrawable> {
        private static final float SCRIM_ADJUSTMENT = 0.075f;
        private final ImageView imageView;

        public ShotRequestListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            final Bitmap bitmap = GlideUtils.getBitmap(resource);
            if (bitmap == null) {
                return false;
            }
            float imageScale = (float) imageView.getHeight() / (float) bitmap.getHeight();
            float heightInDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36,
                    DetailActivity.this.getResources().getDisplayMetrics());
            Palette.from(bitmap)
                    .maximumColorCount(6)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.getWidth() - 1, (int) (heightInDip / imageScale))
                    // - 1 to work around https://code.google.com/p/android/issues/detail?id=191013
                    .generate(palette -> {
                        boolean isDark = false;
                        @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                        if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                        } else {
                            isDark = lightness == ColorUtils.IS_DARK;
                        }

                        // make back icon dark on light images

                        // color the status bar. Set a complementary dark color on L,
                        // light or dark color on M (with matching status bar icons)

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            return;
                        }

                        Palette.Swatch topColor = ColorUtils.getMostPopulousSwatch(palette);
                        int statusBarColor = -1;
                        if (topColor != null &&
                                (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                            statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                    isDark, SCRIM_ADJUSTMENT);
                            // set a light status bar on M+
                            if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ViewUtils.setLightStatusBar(imageView);
                            }
                        }
                        if (statusBarColor == -1) {
                            return;
                        }
                        if (statusBarColor != getWindow().getStatusBarColor()) {
                            //imageView.setScrimColor(statusBarColor);
                            ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(getWindow
                                    ().getStatusBarColor(), statusBarColor);
                            statusBarColorAnim.addUpdateListener
                                    (animation -> getWindow().setStatusBarColor(
                                            (int) animation.getAnimatedValue()));
                            statusBarColorAnim.setDuration(1000);
                            statusBarColorAnim.setInterpolator(new FastOutSlowInInterpolator());
                            statusBarColorAnim.start();
                        }
                    });
            return false;
        }
    }
}
