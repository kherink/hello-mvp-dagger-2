package com.example.bradcampbell.domain;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class HelloEntity implements Parcelable {
    public abstract int value();
    public abstract long timestamp();

    public static HelloEntity create(int value, long timestamp) {
        return new AutoParcel_HelloEntity(value, timestamp);
    }
}
