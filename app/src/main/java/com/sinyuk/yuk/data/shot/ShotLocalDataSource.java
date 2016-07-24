package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by Sinyuk on 16/7/6.
 */
public class ShotLocalDataSource implements ShotDataSource {
    private static final int MAX_BUFFER_SIZE = 10;
    private static ShotLocalDataSource instance = null;
    private final LiteOrm liteOrm;


    private ShotLocalDataSource(LiteOrm liteOrm) {
        Timber.tag("ShotLocalDataSource");
        this.liteOrm = liteOrm;
        /*liteOrm.deleteDatabase();
        liteOrm.openOrCreateDatabase();*/
    }

    public static ShotLocalDataSource getInstance(LiteOrm liteOrm) {
        synchronized (ShotLocalDataSource.class) {
            if (instance == null) { instance = new ShotLocalDataSource(liteOrm); }
            return instance;
        }
    }

    @Override
    public Observable<List<Shot>> getShots(@NonNull String type, @NonNull int page) {
        return Observable.fromCallable(() -> doQuery(type))
                .doOnError(throwable -> Timber.d("get cached shots: " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io())
                .doOnError(throwable -> Timber.d("get cached shots: " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.computation());

    }

    public void saveShots(@NonNull String type, @NonNull List<Shot> data) {
        Observable.from(data)
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .map(shot -> {
                    Timber.d("set type " + shot.getId() + " & " + shot.getUsername());
                    shot.setType(type);
                    return shot;
                })
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .doOnCompleted(() -> getShots(type, 1).map(List::size)
                        .observeOn(Schedulers.io())
                        .subscribe(size -> {
                            if (size > MAX_BUFFER_SIZE) {
                                final int from = 0;
                                final int to = Math.max(0, size - MAX_BUFFER_SIZE - 1);
                                liteOrm.delete(Shot.class, from, to, null);
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(shot -> {
                    Timber.d("insert " + shot.getId() + " & " + shot.getUsername());
                    liteOrm.insert(shot, ConflictAlgorithm.Replace);
                });
//        .toSortedList((shot, shot2) -> shot.getCreatedAt().compareTo(shot2.getCreatedAt()))
    }


    private List<Shot> doQuery(String type) {
        return liteOrm.query(new QueryBuilder<>(Shot.class).where(
                Shot.COL_TYPE + "=?", new String[]{type}));
    }
}
