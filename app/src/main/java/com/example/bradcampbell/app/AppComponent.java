package com.example.bradcampbell.app;

import com.example.bradcampbell.data.HelloDiskCache;
import com.example.bradcampbell.domain.HelloModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = AppModule.class
)
@Singleton
public interface AppComponent {
    HelloModel getHelloModel();
    HelloDiskCache getHelloDiskCache();
}
