package com.sinyuk.yuk.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sinyuk on 16.1.4.
 * 有header和footer的recycleView
 */
public abstract class BaseRVAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = Integer.MAX_VALUE;
    private static final int TYPE_FOOTER = Integer.MAX_VALUE - 1;
    private static final int ITEM_MAX_TYPE = Integer.MAX_VALUE - 2;

    private RecyclerView.ViewHolder headerViewHolder;
    private RecyclerView.ViewHolder footerViewHolder;

    protected List<T> mDataSet = Collections.emptyList();

 /*    public void setHeaderView(View header) {
        if (headerViewHolder == null || header != headerViewHolder.itemView) {
            headerViewHolder = new MyViewHolder(header);
            notifyDataSetChanged();
        }
    }

   public void setHeaderViewFullSpan(View header) {
        if (headerViewHolder == null || header != headerViewHolder.itemView) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            headerViewHolder = new MyViewHolder(header);
            headerViewHolder.itemView.setLayoutParams(layoutParams);
            notifyDataSetChanged();
        }

    }*/

    public void setFooterView(View foot) {
        if (footerViewHolder == null || foot != footerViewHolder.itemView) {
            footerViewHolder = new MyViewHolder(foot);
            notifyDataSetChanged();
        }
    }


/*    public void removeHeader() {
        if (headerViewHolder != null) {
            headerViewHolder = null;
            notifyDataSetChanged();
        }
    }*/

    public void removeFooter() {
        if (footerViewHolder != null) {
            footerViewHolder = null;
            notifyDataSetChanged();
        }
    }


    public boolean hasHeader() {
        return headerViewHolder != null;
    }

    public boolean hasFooter() {
        return footerViewHolder != null;
    }

    private boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    private boolean isFooter(int position) {
        return hasFooter() && position == getDataItemCount() + (hasHeader() ? 1 : 0);
    }

    private int itemPositionInData(int rvPosition) {
        return rvPosition - (hasHeader() ? 1 : 0);
    }

    private int itemPositionInRV(int dataPosition) {
        return dataPosition + (hasHeader() ? 1 : 0);
    }

    @Override
    public int getItemCount() {
        int itemCount = getDataItemCount();

        if (hasHeader()) {
            itemCount += 1;
        }
        if (hasFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    public int getDataItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void notifyMyItemInserted(int itemPosition) {
        notifyItemInserted(itemPositionInRV(itemPosition));
    }

    public void notifyMyItemRemoved(int itemPosition) {
        notifyItemRemoved(itemPositionInRV(itemPosition));
    }

    public void notifyMyItemChanged(int itemPosition) {
        notifyItemChanged(itemPositionInRV(itemPosition));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return headerViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            return footerViewHolder;
        }
        return onCreateMyItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isHeader(position) && !isFooter(position)) {
            onBindMyItemViewHolder((VH) holder, itemPositionInData(position));
        }
    }

    protected abstract void onBindMyItemViewHolder(VH holder, int position);

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        int dataItemType = getDataItemType(itemPositionInData(position));
        if (dataItemType > ITEM_MAX_TYPE) {
            throw new IllegalStateException("getDataItemType() must be less than " + ITEM_MAX_TYPE + ".");
        }
        return dataItemType;
    }

    /**
     * make sure your dataItemType < Integer.MAX_VALUE-1
     *
     * @param position item view position in rv
     * @return item viewType
     */
    public int getDataItemType(int position) {
        return 0;
    }

    public List<T> getDataSet() {
        return mDataSet;
    }

/*    public void setDataSet(List<T> data) {
        mDataSet = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        final DiffUtil.Callback diffCallback = new DiffUtil.Callback (mDataSet, data);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        diffResult.dispatchUpdatesTo(this);
    }*/

    public abstract VH onCreateMyItemViewHolder(ViewGroup parent, int viewType);

    /**
     * ViewHolder for header and footer
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(View v) {
            super(v);
        }
    }


}



