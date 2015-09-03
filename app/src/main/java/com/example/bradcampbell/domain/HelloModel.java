package com.example.bradcampbell.domain;

import com.example.bradcampbell.data.HelloService;
import com.example.bradcampbell.data.HelloDiskCache;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

import static com.example.bradcampbell.util.Rx.applySchedulers;

@Singleton
public class HelloModel {
    private HelloEntity memoryCache;

    @Inject HelloDiskCache diskCache;
    @Inject HelloService networkService;

    @Inject HelloModel() {
    }

    public Observable<HelloEntity> getValue() {
        return Observable.concat(memory(), disk(), network())
                .first(entity -> entity != null && entity.isUpToDate());
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
                .flatMap(diskCache::saveEntity)
                .compose(applySchedulers());
    }

    private Observable<HelloEntity> disk() {
        return diskCache.getEntity()
                .doOnNext(entity -> this.memoryCache = entity)
                .compose(applySchedulers());
    }

    private Observable<HelloEntity> memory() {
        return Observable.just(memoryCache);
    }
}
