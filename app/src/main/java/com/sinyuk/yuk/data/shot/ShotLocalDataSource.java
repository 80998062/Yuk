package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;


/**
 * Created by Sinyuk on 16/7/6.
 */
@Singleton
public class ShotLocalDataSource implements ShotDataSource {

    LruCache<String, List<Shot>> shotCache = null;

    public ShotLocalDataSource() {
        final int fakeCacheSize = 100 * 10;
        shotCache = new LruCache<String, List<Shot>>(fakeCacheSize) {
            @Override
            protected int sizeOf(String key, List<Shot> value) {
                return 100;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, List<Shot> oldValue, List<Shot> newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };

    }

    @Override
    public Observable getShots(@NonNull String type, @NonNull int page) {
        return Observable.from(shotCache.get(type))
                .toSortedList((shot1, shot2) -> {
                    return shot1.getCreatedDate().compareTo(shot2.getCreatedDate());
                });
    }

    public void saveShots(@NonNull String type, @NonNull List<Shot> data) {
        if (shotCache.get(type) != null) {
            shotCache.remove(type);
        }

        shotCache.put(type, data);
    }
}
