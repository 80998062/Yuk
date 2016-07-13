package com.sinyuk.yuk;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Sinyuk on 16/7/13.
 */
public class RxDemo {

    public static void main(String[] args) {

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();

        List<String> list = new ArrayList<>();
        list3.add("hello");
        list3.add("world");
        list2.add("hello");
        list.add("default");

        test(list1, list2, list3, list);
    }

    private static void test(List<String> list1, List<String> list2, List<String> list3, List<String> list) {
        Observable.just("A")
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("call "+s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s+"B";
            }
        }).observeOn(Schedulers.immediate())
                .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext "+s);
            }
        });


    }
}
