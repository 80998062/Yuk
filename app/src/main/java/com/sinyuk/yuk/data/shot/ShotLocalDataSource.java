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
    private static final int MAX_BUFFER_SIZE = 20;
    private static ShotLocalDataSource instance = null;
    private final LiteOrm liteOrm;


    private ShotLocalDataSource(LiteOrm liteOrm) {
        Timber.tag("ShotLocalDataSource");
        this.liteOrm = liteOrm;
    }

    public static ShotLocalDataSource getInstance(LiteOrm liteOrm) {
        synchronized (ShotLocalDataSource.class) {
            if (instance == null) { instance = new ShotLocalDataSource(liteOrm); }
            return instance;
        }
    }

    @Override
    public Observable<List<Shot>> getShots(@NonNull String type, @NonNull int page) {
        Timber.d("getShots type %s page %d", type, page);

        return Observable.fromCallable(() -> doQuery(type))
                .doOnError(throwable -> Timber.d("get cached shots: " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .toSortedList((shot, shot2) -> {
                    // 按时间 降序之后再输出
                    return -shot.getCreatedDate().compareTo(shot2.getCreatedDate());
                })
                .doOnError(throwable -> Timber.d("get cached shots: " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.computation());

    }

    public void saveShots(@NonNull String type, @NonNull List<Shot> data) {
        Observable.from(data)
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .map(shot -> {
                    shot.setType(type);
                    return shot;
                })
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .doOnCompleted(() -> getShots(type, 1).map(List::size)
                        .observeOn(Schedulers.io())
                        .subscribe(size -> {
                            if (size > MAX_BUFFER_SIZE + 1) {
                                liteOrm.delete(Shot.class, MAX_BUFFER_SIZE, size - 1, null);
                            }
                        }))
                .observeOn(Schedulers.io())
                .subscribe(shot -> {
                    liteOrm.insert(shot, ConflictAlgorithm.Replace);
                    Timber.d("insert %s", shot.getType());
                });
    }


    private List<Shot> doQuery(String type) {
        return liteOrm.query(new QueryBuilder<>(Shot.class).where(
                Shot.COL_TYPE + "=?", new String[]{type}));
    }
}
