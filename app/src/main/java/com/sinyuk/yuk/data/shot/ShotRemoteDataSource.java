package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.sinyuk.yuk.api.DribbleService;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class ShotRemoteDataSource implements ShotDataSource {

    DribbleService mDribbleService;

    ShotLocalDataSource localDataSource;


    public ShotRemoteDataSource(DribbleService dribbleService, ShotLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
        this.mDribbleService = dribbleService;
        Timber.tag("ShotRemoteDataSource");
    }

    /**
     * @param type
     * @param page
     * @return
     */
    @Override
    public Observable<List<Shot>> getShots(@NonNull String type, @NonNull int page) {
        Timber.d("getShots");
        return mDribbleService.shots(type, page)
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .doOnNext(shots -> {if (page == 1) { localDataSource.saveShots(type, shots); }})
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(throwable -> {
                    Timber.d(throwable.getLocalizedMessage());
                    return Observable.just(Collections.emptyList());
                });

    }


}
