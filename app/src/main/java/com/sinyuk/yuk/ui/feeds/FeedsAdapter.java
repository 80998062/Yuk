package com.sinyuk.yuk.ui.feeds;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinyuk.yuk.R;
import com.sinyuk.yuk.data.BaseRVAdapter;
import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.widgets.RatioImageView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class FeedsAdapter extends BaseRVAdapter<Shot> {


    public FeedsAdapter(Context context, ArrayList<Shot> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected void footerOnVisibleItem() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateMyItemViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder(View.inflate(mContext, R.layout.feed_list_item, parent));
    }

    @Override
    public void onBindMyItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class FeedItemViewHolder extends RecyclerView.ViewHolder {

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
        }
    }
}
