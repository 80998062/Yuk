package com.sinyuk.yuk.api;

import com.sinyuk.yuk.data.shot.Shot;
import com.sinyuk.yuk.data.user.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sinyuk on 16.6.16.
 */
public interface DribbleService {

//    @Headers("Cache-Control: public,max-age=10,only-if-cached")
    @GET("shots?per_page=5")
    Observable<List<Shot>> shots(@Query("list") String listType, @Query("page") int pageIndex);

    @GET("user")
    Observable<User> getAuthenticatedUser();


}
