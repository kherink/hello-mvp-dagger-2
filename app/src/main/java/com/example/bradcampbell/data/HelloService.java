package com.example.bradcampbell.data;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * This is faking a Retrofit service returning an observable
 */
public class HelloService {
    public Observable<Integer> getValue() {
        return Observable.defer(() -> {
                    Random rand = new Random();
                    return Observable.just(rand.nextInt(50));
                })
                .delay(3, TimeUnit.SECONDS);
    }
}
