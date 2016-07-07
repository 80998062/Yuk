package com.sinyuk.yuk.data.shot;

import android.support.annotation.NonNull;

import com.sinyuk.yuk.api.DribbleService;

import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class ShotRemoteDataSource implements ShotDataSource {

    DribbleService mDribbleService;

    ShotLocalDataSource localDataSource;

    public ShotRemoteDataSource(DribbleService dribbleService, ShotLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
        this.mDribbleService = dribbleService;
    }

    /**
     * @param type
     * @param page
     * @return
     */
    @Override
    public Observable getShots(@NonNull String type, @NonNull int page) {
        return mDribbleService.shots(type, page)
                .subscribeOn(Schedulers.io())
                .flatMap(shots -> {
                    // 同时保存数据到本地 只保存第一页
                    if (page == 1) { localDataSource.saveShots(type, shots); }
                    return Observable.from(shots);
                }).subscribeOn(Schedulers.io());
    }
}
