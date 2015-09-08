package com.example.bradcampbell;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.bradcampbell.data.HelloDiskCache;
import com.example.bradcampbell.data.HelloService;
import com.example.bradcampbell.domain.Clock;
import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.domain.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides @Singleton public HelloModel provideHelloModel(SchedulerProvider schedulerProvider,
                                                             HelloDiskCache helloDiskCache,
                                                             HelloService helloService,
                                                             Clock clock) {
        return new HelloModel(schedulerProvider, helloDiskCache, helloService, clock);
    }

    @Provides public HelloDiskCache provideHelloDiskCache(SharedPreferences prefs) {
        return new HelloDiskCache(prefs);
    }

    @Provides public HelloService provideHelloService() {
        return new HelloService();
    }

    @Provides public SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.DEFAULT;
    }

    @Provides public Clock provideClock() {
        return Clock.REAL;
    }
}
