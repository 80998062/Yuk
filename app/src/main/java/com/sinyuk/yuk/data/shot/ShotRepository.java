package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.utils.PrefsUtils;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.20.
 */
public class ShotRepository {

    private final RxSharedPreferences prefs;

    private final ShotRemoteDataSource remoteDataSource;

    public ShotRepository(ShotRemoteDataSource remoteDataSource,
                          RxSharedPreferences preferences) {
        this.remoteDataSource = remoteDataSource;
        this.prefs = preferences;
        Timber.tag("ShotRepository");
    }

    public Observable getShots(@NonNull String type,int page) {
        Observable<List<Shot>> remoteObservable = remoteDataSource.getShots(type, page);
        return prefs.getBoolean(PrefsUtils.auto_refresh, true).asObservable()
                .flatMap(autoRefresh -> remoteObservable.doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                        .onErrorResumeNext(Observable.just(Collections.emptyList()))
                        .observeOn(AndroidSchedulers.mainThread()));


    }


}
