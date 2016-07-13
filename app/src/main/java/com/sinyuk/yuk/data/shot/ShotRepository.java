package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.20.
 */
public class ShotRepository {

    ShotRemoteDataSource remoteDataSource;

    ShotLocalDataSource localDataSource;

    public ShotRepository(ShotLocalDataSource localDataSource, ShotRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        Timber.tag("ShotRepository");
    }

    public Observable getShots(@NonNull String type, @NonNull int page) {
        Observable<List<Shot>> localObservable = localDataSource.getShots(type, page);
        Observable<List<Shot>> remoteObservable = remoteDataSource.getShots(type, page);
        return Observable.concat(remoteObservable, localObservable)
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .firstOrDefault(Collections.emptyList(), shots -> !shots.isEmpty())
                .observeOn(AndroidSchedulers.mainThread());

    }


}
