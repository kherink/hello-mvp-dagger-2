package com.example.bradcampbell.domain;

import com.example.bradcampbell.data.HelloDiskCache;
import com.example.bradcampbell.data.HelloService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class HelloModel {
    private static final long STALE_MS = 5 * 1000;

    HelloEntity memoryCache;

    @Inject SchedulerProvider schedulerProvider;
    @Inject HelloDiskCache diskCache;
    @Inject HelloService networkService;
    @Inject Clock clock;

    @Inject HelloModel() {
    }

    public Observable<HelloEntity> getValue() {
        return Observable.concat(memory(), disk(), network())
                .first(entity -> entity != null && isUpToDate(entity));
    }

    public Observable<Void> clearMemoryCache() {
        return Observable.defer(() -> {
            memoryCache = null;
            return Observable.just(null);
        });
    }

    public Observable<Void> clearMemoryAndDiskCache() {
        return diskCache.clear()
                .doOnNext(__ -> memoryCache = null);
    }

    private Observable<HelloEntity> network() {
        return networkService.getValue()
                .doOnNext(entity -> this.memoryCache = entity)
                .flatMap(entity -> diskCache.saveEntity(entity).map(__ -> entity))
                .compose(schedulerProvider.applySchedulers());
    }

    private Observable<HelloEntity> disk() {
        return diskCache.getEntity()
                .doOnNext(entity -> this.memoryCache = entity)
                .compose(schedulerProvider.applySchedulers());
    }

    private Observable<HelloEntity> memory() {
        return Observable.just(memoryCache);
    }

    private boolean isUpToDate(HelloEntity entity) {
        return clock.millis() - entity.timestamp() < STALE_MS;
    }
}
