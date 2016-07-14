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

    private final ShotLocalDataSource localDataSource;

    public ShotRepository(ShotLocalDataSource localDataSource,
                          ShotRemoteDataSource remoteDataSource,
                          RxSharedPreferences preferences) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.prefs = preferences;
        Timber.tag("ShotRepository");
    }

    public Observable getShots(@NonNull String type, @NonNull int page) {
        Observable<List<Shot>> localObservable = localDataSource.getShots(type, page);
        Observable<List<Shot>> remoteObservable = remoteDataSource.getShots(type, page);
        return prefs.getBoolean(PrefsUtils.auto_refresh, true).asObservable()
                .flatMap(autoRefresh -> {
                    Observable<List<Shot>> observable = autoRefresh
                            ? Observable.concat(remoteObservable, localObservable)
                            : Observable.concat(localObservable, remoteObservable);

                    return observable.doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                            .firstOrDefault(Collections.emptyList(), shots -> !shots.isEmpty())
                            .observeOn(AndroidSchedulers.mainThread());
                });


    }


}
