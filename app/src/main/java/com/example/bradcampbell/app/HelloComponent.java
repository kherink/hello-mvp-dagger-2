package com.example.bradcampbell.app;

import com.example.bradcampbell.presentation.HelloPresenter;

import dagger.Component;
import nz.bradcampbell.compartment.HasPresenter;

@Component(
        dependencies = AppComponent.class
)
@HelloScope
public interface HelloComponent extends HasPresenter<HelloPresenter> {
    void inject(HelloFragment fragment);
}
