package com.example.bradcampbell.domain;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class HelloEntity implements Parcelable {
    private static final long STALE_MS = 5 * 1000;

    public abstract int value();

    private long timestamp;

    public static HelloEntity create(int value) {
        HelloEntity entity = new AutoParcel_Hello1Entity(value);
        entity.timestamp = System.currentTimeMillis();
        return entity;
    }

    public static HelloEntity create(int value, long timestamp) {
        HelloEntity entity = new AutoParcel_Hello1Entity(value);
        entity.timestamp = timestamp;
        return entity;
    }

    public boolean isUpToDate() {
        return System.currentTimeMillis() - timestamp < STALE_MS;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
