package com.example.bradcampbell;

import com.example.bradcampbell.data.HelloDiskCache;
import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = AppModule.class
)
@Singleton
public interface AppComponent {
    HelloModel getHelloModel();
    HelloDiskCache getHelloDiskCache();
    MainActivity inject(MainActivity activity);
}
