package com.sinyuk.yuk;

import android.os.Bundle;
import android.util.Log;

import com.sinyuk.yuk.ui.BaseActivity;

public class RetrofitDemo extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



/*        DribbleService dribbleService = mRetrofit.create(DribbleService.class);

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
                    public void onNext(List<Shot> feeds) {
                        for (int i = 0; i < feeds.size(); i++) {
                            log(feeds.get(i).toString());
                        }
                    }
                });*/


    }

    @Override
    protected int getContentViewID() {
        return 0;
    }

    @Override
    protected void beforeInflating() {

    }

    @Override
    protected void finishInflating() {

    }

    public void log(String msg) {
        Log.w("Sinyuk", msg);
    }
}
