package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.yuk.api.DribbleService;
import com.sinyuk.yuk.utils.PrefsUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16.6.20.
 */
public class ShotRepository {
    private final HashMap<String, LinkedList<Shot>> mShotCache = new HashMap<>();
    private final RxSharedPreferences prefs;
    DribbleService mDribbleService;

    public ShotRepository(DribbleService dribbleService,
                          RxSharedPreferences preferences) {
        this.mDribbleService = dribbleService;
        this.prefs = preferences;
        Timber.tag("ShotRepository");
    }

    public Observable getShots(@NonNull String type, int page) {
        Observable<List<Shot>> cachingObservable = mDribbleService.shots(type, page);
        return prefs.getBoolean(PrefsUtils.auto_caching, true)
                .asObservable()
                .flatMap(new Func1<Boolean, Observable<?>>() {
                    @Override
                    public Observable<?> call(Boolean autoCaching) {
                        return cachingObservable
                                .map(insertShots(page, type))
                                .doOnError(throwable -> Timber.e(throwable.getMessage()))
                                .subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    private Func1<List<Shot>, List<Shot>> insertShots(int page, String type) {
        return result -> {
            LinkedList<Shot> list = mShotCache.get(type);
            if (list == null) {
                list = new LinkedList<>();
            }
            Timber.d("Shots in cache :" + list.toString());
            if (page == 1) {
                if (Collections.disjoint(list, result)) {
                    list.clear();
                    list.addAll(result);
                } else {
                    result.removeAll(list);
                    list.addAll(0, result);
                }
            } else {
                list.addAll(result);    // append to front
            }
            mShotCache.put(type, list);
            return list;
        };
    }
}
