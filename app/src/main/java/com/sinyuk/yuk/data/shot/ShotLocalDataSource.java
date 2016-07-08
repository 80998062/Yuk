package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Created by Sinyuk on 16/7/6.
 */
public class ShotLocalDataSource implements ShotDataSource {

    LruCache<String, List<Shot>> shotCache = null;
    List<Shot> emptyList = Collections.emptyList();

    public ShotLocalDataSource() {
        final int fakeCacheSize = 100 * 10;
        shotCache = new LruCache<String, List<Shot>>(fakeCacheSize) {
            @Override
            protected int sizeOf(String key, List<Shot> value) {
                return 10;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, List<Shot> oldValue, List<Shot> newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };

    }

    @Override
    public Observable<List<Shot>> getShots(@NonNull String type, @NonNull int page) {
        if (shotCache.get(type) != null && !shotCache.get(type).isEmpty()) {
            return Observable.from(shotCache.get(type))
                    .subscribeOn(Schedulers.io())
                    .toSortedList((shot, shot2) -> {
                        return shot.getCreatedDate().compareTo(shot2.getCreatedDate());
                    }).subscribeOn(Schedulers.computation());
        } else {
            return Observable.just(emptyList);
        }
    }

    public void saveShots(@NonNull String type, @NonNull List<Shot> data) {
        if (shotCache.get(type) != null) {
            shotCache.remove(type);
        }

        shotCache.put(type, data);
    }
}
