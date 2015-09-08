package com.example.bradcampbell.ui;

import com.example.bradcampbell.AppComponent;
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
