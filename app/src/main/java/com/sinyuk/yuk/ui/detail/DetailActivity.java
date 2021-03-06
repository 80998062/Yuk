package com.sinyuk.yuk.ui.detail;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.utils.ColorUtils;
import com.sinyuk.yuk.utils.Preconditions;
import com.sinyuk.yuk.utils.StringUtils;
import com.sinyuk.yuk.utils.ViewUtils;
import com.sinyuk.yuk.utils.glide.CropCircleTransformation;
import com.sinyuk.yuk.utils.glide.GlideUtils;
import com.sinyuk.yuk.widgets.FourThreeImageView;
import com.sinyuk.yuk.widgets.TextDrawable;

import butterknife.BindColor;
import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/8/20.
 */
public class DetailActivity extends BaseActivity {
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
        Timber.tag("DetailActivity");
    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {
        loadShot();
        loadAuthorInformation();
    }

    // 作者信息
    private void loadAuthorInformation() {
        final String username = StringUtils.valueOrDefault(mData.getUser().getUsername(), " ");
        // use a TextDrawable as a placeholder
        final char firstLetter = username.charAt(0);

        Timber.d("Author name : %s", username);
        final TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(firstLetter + "", COLOR_SLATE);

        Timber.d("Avatar url : %s", mData.getUser().getAvatarUrl());
        Glide.with(this).load(mData.getUser().getAvatarUrl())
                .bitmapTransform(new CropCircleTransformation(this)).placeholder(textDrawable)
                .error(textDrawable)
                .into(mAvatar);

        /*username*/
        setText(mPublishInfo, username);
    }

    // 加载图片
    private void loadShot() {
        DrawableRequestBuilder<String> requestBuilder = Glide.with(this).fromString().crossFade(CROSS_FADE_DURATION).centerCrop();
        if (mData.isAnimated()) {
            requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        } else {
            requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
        }
        requestBuilder.load(mData.bestQuality()).listener(new ShotRequestListener(mShot)).into(mShot);

        //title
        mTitle.setText(StringUtils.valueOrDefault(mData.getTitle(), ""));
    }

    private void setText(TextView v, String text) {
        Preconditions.checkNotNull(v);
        Preconditions.checkNotNull(text);
        v.setText(text);
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
            if (bitmap == null) { return false; }
            float imageScale = (float) imageView.getHeight() / (float) bitmap.getHeight();
            float twentyFourDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                    DetailActivity.this.getResources().getDisplayMetrics());
            Palette.from(bitmap)
                    .maximumColorCount(4)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.getWidth() - 1, (int) (twentyFourDip / imageScale))
                    // - 1 to work around https://code.google.com/p/android/issues/detail?id=191013
                    .generate(palette -> {
                        boolean isDark;
                        @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                        if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                        } else {
                            isDark = lightness == ColorUtils.IS_DARK;
                        }

                        if (!isDark) { // make back icon dark on light images
                           /* back.setColorFilter(ContextCompat.getColor(
                                    DribbbleShot.this, R.color.dark_icon));*/
                        }

                        // color the status bar. Set a complementary dark color on L,
                        // light or dark color on M (with matching status bar icons)

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { return; }

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
                        if (statusBarColor == -1) { return; }
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
