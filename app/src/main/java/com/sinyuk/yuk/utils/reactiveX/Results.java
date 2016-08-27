package com.sinyuk.yuk.utils.reactiveX;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

public final class Results {
    private static final Func1<Result<?>, Boolean> SUCCESSFUL =
            result -> !result.isError()
                    && result.error() == null
                    && result.response().isSuccessful()
                    && result.response().body() != null;

    private Results() {
        throw new AssertionError("No instances.");
    }

    public static Func1<Result<?>, Boolean> isSuccessful() {
        return SUCCESSFUL;
    }
}
