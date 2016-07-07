package com.sinyuk.yuk.api;

import com.sinyuk.yuk.data.shot.Shot;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sinyuk on 16.6.16.
 */
public interface DribbleService {

    @GET("shots")
    Observable<List<Shot>> shots(@Query("list") String listType, @Query("page") int pageIndex);

}
