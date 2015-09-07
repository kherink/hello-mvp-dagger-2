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
        return Observable.defer(() -> {
                    Observable<HelloEntity> result;
                    int data = prefs.getInt(KEY_DATA, -1);
                    if (data != -1) {
                        long timestamp = prefs.getLong(KEY_TIMESTAMP, 0L);
                        result = Observable.just(HelloEntity.create(data, timestamp));
                    } else {
                        result = Observable.just(null);
                    }
                    return result;
                });
    }

    public Observable<Void> saveEntity(HelloEntity value) {
        return Observable.defer(() -> {
            prefs.edit().putInt(KEY_DATA, value.value()).commit();
            prefs.edit().putLong(KEY_TIMESTAMP, value.timestamp()).commit();
            return Observable.just(null);
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
