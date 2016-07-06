package com.sinyuk.yuk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Sinyuk on 16/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static String TAG = "";
    protected CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();

        beforeInflating();

        if (getContentViewID() != 0) {
            setContentView(getContentViewID());
        } else {
            throw new IllegalArgumentException(TAG + " -> contentView can not been set");
        }

        unbinder = ButterKnife.bind(this);

        finishInflating();

     /*   mLazyLoadRunnable = this::lazyLoad;
        if (savedInstanceState == null) {
            getWindow().getDecorView().post(() -> myHandler.postDelayed(mLazyLoadRunnable, LAZY_LOAD_DELAY));
        }*/

    }

    protected abstract int getContentViewID();

    protected abstract void beforeInflating();

    protected abstract void finishInflating();



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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }
}
