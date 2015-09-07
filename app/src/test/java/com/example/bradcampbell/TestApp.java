package com.example.bradcampbell;

import com.example.bradcampbell.app.App;
import com.example.bradcampbell.app.AppModule;

public class TestApp extends App {
    private AppModule overrideModule;

    @Override protected AppModule getApplicationModule() {
        return overrideModule != null ? overrideModule : super.getApplicationModule();
    }

    public void setOverrideModule(AppModule overrideModule) {
        this.overrideModule = overrideModule;
    }
}
