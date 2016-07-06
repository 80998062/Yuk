package com.sinyuk.yuk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Sinyuk on 16/7/6.
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    private CompositeSubscription mCompositeSubscription;
    protected Context mContext = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
        beforeInflate();
    }

    protected abstract void beforeInflate();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getRootViewId(), container, false);
    }

    protected abstract int getRootViewId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        finishInflate();
    }

    protected abstract void finishInflate();

    protected void addSubscription(Subscription s) {
        mCompositeSubscription.add(s);
    }

    protected void removeSubscription(Subscription s) {
        mCompositeSubscription.remove(s);
    }

    protected void clearSubscription() {
        mCompositeSubscription.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (!mCompositeSubscription.isUnsubscribed()) { mCompositeSubscription.unsubscribe(); }

    }


}
