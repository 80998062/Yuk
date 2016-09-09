package com.sinyuk.yuk.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.DribbleService;
import com.sinyuk.yuk.data.shot.Attachment;
import com.sinyuk.yuk.ui.BaseFragment;
import com.sinyuk.yuk.utils.lists.GravitySnapHelper;
import com.sinyuk.yuk.widgets.FontTextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sinyuk on 16/9/9.
 */
public class AttachmentFragment extends BaseFragment {
    private static final String KEY_ID = "ID";
    private static final String KEY_COUNT = "COUNT";
    private static final String TAG = "AttachmentFragment";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    DribbleService dribbleService;
    private long mId;
    private int mCount;

    public static AttachmentFragment newInstance(long id, int attachmentCount) {
        Bundle args = new Bundle();
        args.putLong(KEY_ID, id);
        args.putInt(KEY_COUNT, attachmentCount);

        AttachmentFragment fragment = new AttachmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void beforeInflate() {
        mId = getArguments().getLong(KEY_ID);
        mCount = getArguments().getInt(KEY_COUNT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().inject(this);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.detail_activity_attachment_fragment;
    }

    @Override
    protected void finishInflate() {
        initRecyclerView();
        createPlaceHolder();
        loadAttachments();
    }

    private void loadAttachments() {
        dribbleService.attachments(mId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Attachment>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Attachment> attachments) {
                        for (int i = 0; i < attachments.size(); i++) {
                            Log.d(TAG, "onNext:");
                            Log.d(TAG, attachments.get(i).toString());
                        }

                    }
                });
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        final SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);

        snapHelperStart.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);

//        mRecyclerView.addItemDecoration(new FeedsItemDecoration(getContext()));
    }

    private void createPlaceHolder() {

    }

    private class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

        @Override
        public AttachmentAdapter.AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AttachmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_activity_attachment_item, parent, false));
        }

        @Override
        public void onBindViewHolder(AttachmentViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return mCount;
        }

        public class AttachmentViewHolder extends RecyclerView.ViewHolder {

            public AttachmentViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
