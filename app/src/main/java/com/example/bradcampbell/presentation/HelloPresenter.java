package com.example.bradcampbell.presentation;

import com.example.bradcampbell.app.HelloScope;
import com.example.bradcampbell.domain.HelloEntity;
import com.example.bradcampbell.domain.HelloModel;

import javax.inject.Inject;

import nz.bradcampbell.compartment.BasePresenter;
import rx.Observer;
import rx.Subscription;

@HelloScope
public class HelloPresenter extends BasePresenter<HelloView> {
    @Inject HelloModel model;

    @Inject HelloPresenter() {
    }

    private Subscription subscription;

    public void load() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            return;
        }
        getView().showLoading();
        subscription = model.getValue()
                .subscribe(observer);
    }

    public void clearMemoryCache() {
        model.clearMemoryCache().subscribe();
    }

    public void clearMemoryAndDiskCache() {
        model.clearMemoryAndDiskCache().subscribe();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private Observer<HelloEntity> observer = new Observer<HelloEntity>() {
        @Override public void onCompleted() {
            if (getView() != null) {
                getView().hideLoading();
            }
        }

        @Override public void onError(Throwable e) {
            // TODO: handle error
        }

        @Override public void onNext(HelloEntity entity) {
            if (getView() != null) {
                getView().display("" + entity.value());
            }
        }
    };
}