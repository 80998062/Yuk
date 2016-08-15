package com.sinyuk.yuk.api;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

/**
 * 网络请求的结果
 */
public final class Results {
  private static final Func1<Result<?>, Boolean> SUCCESSFUL =
          result -> !result.isError() && result.response().isSuccessful();

  public static Func1<Result<?>, Boolean> isSuccessful() {
    return SUCCESSFUL;
  }

  private Results() {
    throw new AssertionError("No instances.");
  }
}