package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
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
        return Observable.fromCallable(() -> queryShot(type))
                .doOnError(throwable -> Timber.d("get cached shots: " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io())
                .concatMap(shots -> {
                    if (shots.size() > 10) {
                        return Observable.just(shots).takeLast(10);
                    } else {
                        return Observable.just(shots);
                    }
                })
                .doOnError(throwable -> Timber.d("get cached shots: " + throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io());
    }


    public void saveShots(@NonNull String type, @NonNull List<Shot> data) {
        Observable.from(data)
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .map(shot -> {
                    shot.setType(type);
                    return shot;
                })
                .doOnError(throwable -> Timber.d(throwable.getLocalizedMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(shot -> {
                    liteOrm.insert(shot, ConflictAlgorithm.Replace);
                });
    }

/*    public void checkCacheSize(String type){
        Observable.fromCallable(() -> getShots(type, 1)
                .map(shots -> shots.size() > MAX_BUFFER_SIZE)
                .observeOn(Schedulers.io())
                .subscribe(overflow -> {
                    if (overflow) {
                        deleteCache(type);
                    }
                }))
    }*/

    private List<Shot> queryShot(String type) {
        return liteOrm.query(new QueryBuilder<>(Shot.class).where(
                Shot.COL_TYPE + "=?", new String[]{type}));
    }

    private void deleteShot(String type) {
        liteOrm.delete(new WhereBuilder(Shot.class)
                .where(Shot.COL_TYPE + "=?", new String[]{type})
                .and()
                .greaterThan(Shot.COL_INDEX, 0)
                .and()
                .lessThan(Shot.COL_INDEX, 11));
    }
}
