package com.example.bradcampbell.data;

import android.content.SharedPreferences;

import com.example.bradcampbell.domain.HelloEntity;

import javax.inject.Inject;

import rx.Observable;

public class HelloDiskCache {
    private static final String KEY_DATA = "value-xyz-data";
    private static final String KEY_TIMESTAMP = "value-xyz-timestamp";

    private SharedPreferences prefs;

    @Inject public HelloDiskCache(SharedPreferences sharedPreferences) {
        this.prefs = sharedPreferences;
    }

    public Observable<HelloEntity> getEntity() {
        return Observable.defer(() ->
                Observable.just(HelloEntity.create(prefs.getInt(KEY_DATA, -1),
                        prefs.getLong(KEY_TIMESTAMP, 0L))));
    }

    public Observable<HelloEntity> saveEntity(HelloEntity value) {
        return Observable.defer(() -> {
            prefs.edit().putInt(KEY_DATA, value.value()).commit();
            prefs.edit().putLong(KEY_TIMESTAMP, value.getTimestamp()).commit();
            return Observable.just(value);
        });
    }

    public Observable<Void> clear() {
        return Observable.defer(() -> {
            prefs.edit().remove(KEY_DATA).commit();
            prefs.edit().remove(KEY_TIMESTAMP).commit();
            return Observable.just(null);
        });
    }
}
