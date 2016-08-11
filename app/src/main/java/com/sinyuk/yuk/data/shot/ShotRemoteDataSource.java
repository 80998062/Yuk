package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.sinyuk.yuk.api.DribbleService;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class ShotRemoteDataSource {

    DribbleService mDribbleService;

    public ShotRemoteDataSource(DribbleService dribbleService) {
        this.mDribbleService = dribbleService;
        Timber.tag("ShotRemoteDataSource");
    }

    public Observable<List<Shot>> getShots(@NonNull String type,int page) {
        return mDribbleService.shots(type, page)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    Timber.d(throwable.getLocalizedMessage());
                    return Observable.just(Collections.emptyList());
                });

    }



}
