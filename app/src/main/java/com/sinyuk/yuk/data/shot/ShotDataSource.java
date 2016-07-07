package com.sinyuk.yuk.data.shot;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;

/**
 * Created by Sinyuk on 16/7/6.
 */
public interface ShotDataSource {

    Observable getShots(@NonNull String type, @NonNull int page);
}
