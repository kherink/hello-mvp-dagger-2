package com.example.bradcampbell.app;

import android.content.SharedPreferences;

import com.example.bradcampbell.domain.HelloModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = AppModule.class
)
@Singleton
public interface AppComponent {
    SharedPreferences getSharedPreferences();
    HelloModel getHello1Model();
}
