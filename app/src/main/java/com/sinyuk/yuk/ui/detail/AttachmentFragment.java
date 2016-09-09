package com.sinyuk.yuk.ui.detail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sinyuk.yuk.R;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.widgets.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sinyuk on 16/9/9.
 */
public class AttachmentFragment extends BaseFragment {

    @BindView(R.id.attachment_tv)
    FontTextView mAttachmentTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.root_view)
    LinearLayout mRootView;

    @Override
    protected void beforeInflate() {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.attachment_fragment;
    }

    @Override
    protected void finishInflate() {
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {

    }

    private void initData() {

    }
}
