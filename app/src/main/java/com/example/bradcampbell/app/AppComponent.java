package com.example.bradcampbell.app;

import android.content.SharedPreferences;

import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.domain.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = AppModule.class
)
@Singleton
public interface AppComponent {
    SharedPreferences getSharedPreferences();
    HelloModel getHelloModel();
    SchedulerProvider getSchedulerProvider();
}
