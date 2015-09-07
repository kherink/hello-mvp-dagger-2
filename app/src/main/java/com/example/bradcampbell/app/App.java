package com.example.bradcampbell.app;

import android.content.Context;

import nz.bradcampbell.compartment.ComponentCacheApplication;

public class App extends ComponentCacheApplication {
    private AppComponent component;

    @Override public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(getApplicationModule())
                .build();
    }

    protected AppModule getApplicationModule() {
        return new AppModule(this);
    }

    public static AppComponent getAppComponent(Context context) {
        return ((App)context.getApplicationContext()).component;
    }
}
