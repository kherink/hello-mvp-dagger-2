package com.example.bradcampbell.presentation;

import com.example.bradcampbell.ui.HelloScope;
import com.example.bradcampbell.domain.HelloEntity;
import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.util.EndObserver;

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

    /**
     * Load data.
     */
    public void load() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            return;
        }
        getView().showLoading();
        subscription = model.getValue()
                .subscribe(observer);
    }

    /**
     * This is for demo purposes only. It would be strange to expose cache clearing methods
     * to the UI. Typically caches would be cleared as a result of some action, not from
     * pressing a button.
     */
    public void clearMemoryCache() {
        model.clearMemoryCache().subscribe();
    }

    /**
     * This is for demo purposes only. It would be strange to expose cache clearing methods
     * to the UI. Typically caches would be cleared as a result of some action, not from
     * pressing a button.
     */
    public void clearMemoryAndDiskCache() {
        model.clearMemoryAndDiskCache().subscribe();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private Observer<HelloEntity> observer = new EndObserver<HelloEntity>() {
        @Override public void onEnd() {
            if (getView() != null) {
                getView().hideLoading();
            }
        }

        @Override public void onNext(HelloEntity entity) {
            if (getView() != null) {
                getView().display("" + entity.value());
            }
        }
    };
}