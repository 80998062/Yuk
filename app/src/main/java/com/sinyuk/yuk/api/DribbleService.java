package com.sinyuk.yuk.api;

import com.sinyuk.yuk.data.feed.Feed;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sinyuk on 16.6.16.
 */
public interface DribbleService {

    @GET("shots")
    Observable<List<Feed>> listAllShots(@Query("page") int pageIndex);

    @GET("shots")
    Observable<List<Feed>> listShots(@Query("list") String listType, @Query("page") int pageIndex);

}
