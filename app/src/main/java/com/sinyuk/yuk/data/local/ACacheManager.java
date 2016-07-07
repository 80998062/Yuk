package com.sinyuk.yuk.data.local;

import com.sinyuk.yuk.data.shot.Shot;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Sinyuk on 16/7/7.
 */
public class ACacheManager {
    private PublishSubject<List<Shot>> mSubject = PublishSubject.create();
    

    private List<Shot> getAll() {
        List<Shot> shots = new ArrayList<>();
        // .. performs the query and fills the result
        return shots;
    }

    public Observable<List<Shot>> getObservable() {
        Observable<List<Shot>> firstTimeObservable =
                Observable.fromCallable(this::getAll);

        return firstTimeObservable.concatWith(mSubject);
    }

    public void insertFeed(Shot shot) {
        // ...
        // performs the insertion on the SQLite helper
        // ...
        List<Shot> result = getAll();
        mSubject.onNext(result);
    }
}
