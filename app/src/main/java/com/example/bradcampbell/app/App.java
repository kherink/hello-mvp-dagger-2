package com.example.bradcampbell.app;

import android.content.Context;

import nz.bradcampbell.compartment.ComponentCacheApplication;

public class App extends ComponentCacheApplication {
    private AppComponent component;

    @Override public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent(Context context) {
        return ((App)context.getApplicationContext()).component;
    }
}
