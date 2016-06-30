package com.sinyuk.yuk.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sinyuk.yuk.App;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by Sinyuk on 16/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void setInjector();
}
