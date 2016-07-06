package com.sinyuk.yuk.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Sinyuk on 16.1.4.
 * 有header和footer的recycleView
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = Integer.MAX_VALUE;
    private static final int TYPE_FOOTER = Integer.MAX_VALUE - 1;
    private static final int ITEM_MAX_TYPE = Integer.MAX_VALUE - 2;
    private RecyclerView.ViewHolder headerViewHolder;
    private RecyclerView.ViewHolder footerViewHolder;

    protected Context mContext;
    private ArrayList<T> mDataSet = new ArrayList<>();

    public BaseRVAdapter(Context context, ArrayList<T> dataSet) {
        this.mContext = context;
        this.mDataSet.addAll(dataSet);
    }

    public void setHeaderView(View header) {
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

    }

    public void setFooterView(View foot) {
        if (footerViewHolder == null || foot != footerViewHolder.itemView) {
            footerViewHolder = new MyViewHolder(foot);
            notifyDataSetChanged();
        }
    }

    public void setFooterViewFullSpan(View foot) {
        if (footerViewHolder == null || foot != footerViewHolder.itemView) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            footerViewHolder = new MyViewHolder(foot);
            footerViewHolder.itemView.setLayoutParams(layoutParams);
            notifyDataSetChanged();
        }

    }

    public void removeHeader() {
        if (headerViewHolder != null) {
            headerViewHolder = null;
            notifyDataSetChanged();
        }
    }

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
        return hasFooter() && position == getItemCount() + (hasHeader() ? 1 : 0);
    }

    private int itemPositionInData(int rvPosition) {
        return rvPosition - (hasHeader() ? 1 : 0);
    }

    private int itemPositionInRV(int dataPosition) {
        return dataPosition + (hasHeader() ? 1 : 0);
    }

    @Override
    public int getItemCount() {
        int itemCount = mDataSet == null ? 0 : mDataSet.size();
        if (hasHeader()) {
            itemCount += 1;
        }
        if (hasFooter()) {
            itemCount += 1;
        }
        return itemCount;
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
            onBindMyItemViewHolder(holder, itemPositionInData(position));
        }

        if (isFooter(position)) {
            footerOnVisibleItem();
        }
    }

    protected abstract void footerOnVisibleItem();

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

    public void reset(ArrayList<T> data) {
        mDataSet = data;
        notifyDataSetChanged();
    }

    public ArrayList<T> getAll() {
        return mDataSet;
    }

    public void append(ArrayList<T> dataSet) {
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void add(int position, T item) {
        if (mDataSet != null && position < mDataSet.size()) {
            mDataSet.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void remove(int position) {
        if (mDataSet != null && position < mDataSet.size()) {
            mDataSet.remove(position);
            notifyItemRemoved(position);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateMyItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindMyItemViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * ViewHolder for header and footer
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(View v) {
            super(v);
        }
    }


}



