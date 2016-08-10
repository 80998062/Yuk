package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.widgets.BadgedFourThreeImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sinyuk on 16/8/10.
 */
public class FeedItemVIew extends RelativeLayout{
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

    public FeedItemVIew(Context context) {
        this(context,null);
    }

    public FeedItemVIew(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FeedItemVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(Shot data, Glide glide){

    }
}
