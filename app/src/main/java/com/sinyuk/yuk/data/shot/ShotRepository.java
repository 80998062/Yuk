package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Sinyuk on 16.6.20.
 */
@Singleton
public class ShotRepository {

    ShotRemoteDataSource remoteDataSource;

    ShotLocalDataSource localDataSource;

    public ShotRepository(ShotLocalDataSource localDataSource, ShotRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public Observable<List<Shot>> getShots(@NonNull String type, @NonNull int page) {
        Observable<List<Shot>> localObservable = localDataSource.getShots(type, page);
        Observable<List<Shot>> remoteObservable = remoteDataSource.getShots(type, page);
        return Observable
                .concat(remoteObservable, localObservable)
                .first(shots -> shots != null && !shots.isEmpty());
    }


}
