package com.example.bradcampbell.data;

import com.example.bradcampbell.domain.Clock;
import com.example.bradcampbell.domain.HelloEntity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class HelloService {
    public Clock clock;

    public HelloService(Clock clock) {
        this.clock = clock;
    }

    public Observable<HelloEntity> getValue() {
        return Observable.defer(() -> {
                    Random rand = new Random();
                    return Observable.just(rand.nextInt(50));
                })
                .delay(3, TimeUnit.SECONDS)
                .map(data -> HelloEntity.create(data, clock.millis()));
    }
}
