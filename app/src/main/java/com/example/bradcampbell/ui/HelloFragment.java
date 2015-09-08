package com.example.bradcampbell.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bradcampbell.R;
import com.example.bradcampbell.presentation.HelloPresenter;
import com.example.bradcampbell.presentation.HelloView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import nz.bradcampbell.compartment.PresenterControllerFragment;
import rx.subjects.PublishSubject;

import static com.example.bradcampbell.App.getAppComponent;

public class HelloFragment extends PresenterControllerFragment<HelloComponent, HelloPresenter> implements HelloView {
    @Inject HelloPresenter presenter;

    @InjectView(R.id.text_view) TextView textView;
    @InjectView(R.id.loading) View loadingView;

    @State boolean isLoading = false;
    @State CharSequence data = null;

    private PublishSubject<Boolean> loadingSubject = PublishSubject.create();
    private PublishSubject<CharSequence> dataSubject = PublishSubject.create();

    @Override protected HelloComponent onCreateNonConfigurationComponent() {
        // Return the component that will live until this fragment is destroyed by the user
        // (i.e. this component instance will survive configuration changes). Can be later
        // retrieved using getComponent()
        return DaggerHelloComponent.builder()
                .appComponent(getAppComponent(getActivity()))
                .build();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject dependencies. Note that we could just use getPresenter() from the base
        // class to get the presenter, but this demonstrates that injecting it works too.
        getComponent().inject(this);

        // Restore all @State annotated members
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hello1, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        loadingSubject.subscribe(loading -> {
            isLoading = loading;
            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            textView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        dataSubject.subscribe(data -> {
            this.data = data;
            textView.setText(data);
        });

        // Update loading state
        loadingSubject.onNext(isLoading);

        // Update data state
        dataSubject.onNext(data);

        // Load data when the user first sees this fragment
        if (savedInstanceState == null) {
            presenter.load();
        }
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state of all @State annotated members
        Icepick.saveInstanceState(this, outState);
    }

    @Override public void display(CharSequence stuff) {
        dataSubject.onNext(stuff);
    }

    @Override public void showLoading() {
        loadingSubject.onNext(true);
    }

    @Override public void hideLoading() {
        loadingSubject.onNext(false);
    }

    @OnClick(R.id.load) public void load() {
        presenter.load();
    }

    @OnClick(R.id.clear_memory_cache) public void clearMemoryCache() {
        presenter.clearMemoryCache();
    }

    @OnClick(R.id.clear_memory_and_disk_cache) public void clearMemoryAndDiskCache() {
        presenter.clearMemoryAndDiskCache();
    }
}
