package com.sinyuk.yuk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sinyuk.yuk.api.DribbleService;
import com.sinyuk.yuk.data.feed.Feed;
import com.sinyuk.yuk.mvp.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitDemo extends BaseActivity {

    @Inject
    Retrofit mRetrofit;

    @Inject
    EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_demo);
        // assign singleton instances to fields
        // We need to cast to `MyApp` in order to get the right method
        ((App)getApplication()).getApiComponent().inject(this);


        DribbleService dribbleService = mRetrofit.create(DribbleService.class);

        dribbleService.listAllShots(1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Feed>>() {
                    @Override
                    public void onCompleted() {
                        log("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        log("onError -> " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<Feed> feeds) {
                        for (int i = 0; i < feeds.size(); i++) {
                            log(feeds.get(i).toString());
                        }
                    }
                });


    }

    @Override
    protected void setInjector() {

    }

    public void log(String msg) {
        Log.w("Sinyuk", msg);
    }
}
