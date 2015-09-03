package com.example.bradcampbell.data;

import com.example.bradcampbell.domain.HelloEntity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

public class HelloService {
    @Inject HelloService() {
    }

    public Observable<HelloEntity> getValue() {
        return Observable.defer(() -> {
                    Random rand = new Random();
                    return Observable.just(rand.nextInt(50));
                })
                .delay(3, TimeUnit.SECONDS)
                .map(HelloEntity::create);
    }
}
