package com.example.bradcampbell.app;

import android.content.Context;

import nz.bradcampbell.compartment.ComponentCacheApplication;

public class App extends ComponentCacheApplication {
    private AppComponent component;

    protected AppModule getApplicationModule() {
        return new AppModule(this);
    }

    public static AppComponent getAppComponent(Context context) {
        App app = (App)context.getApplicationContext();
        if (app.component == null) {
            app.component = DaggerAppComponent.builder()
                    .appModule(app.getApplicationModule())
                    .build();
        }
        return app.component;
    }

    public static void clearAppComponent(Context context) {
        App app = (App)context.getApplicationContext();
        app.component = null;
    }
}
