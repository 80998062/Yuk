package com.sinyuk.yuk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sinyuk.yuk.api.DribbleClient;
import com.sinyuk.yuk.api.DribbleService;
import com.sinyuk.yuk.entities.Shot;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_demo);

        DribbleService dribbleService = DribbleClient.getInstance(this).create(DribbleService.class);

        dribbleService.listAllShots(1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Shot>>() {
                    @Override
                    public void onCompleted() {
                        log("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        log("onError -> " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<Shot> shots) {
                        for (int i = 0; i < shots.size(); i++) {
                            log(shots.get(i).toString());
                        }
                    }
                });
    }

    public void log(String msg) {
        Log.w("Sinyuk", msg);
    }
}
