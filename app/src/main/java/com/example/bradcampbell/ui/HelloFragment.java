package com.example.bradcampbell.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.bradcampbell.App;
import com.example.bradcampbell.R;
import com.example.bradcampbell.presentation.HelloPresenter;
import com.example.bradcampbell.presentation.HelloView;
import icepick.Icepick;
import icepick.State;
import rx.subjects.PublishSubject;

import javax.inject.Inject;

public class HelloFragment extends Fragment implements HelloView {
  @Inject HelloPresenter presenter;

  @Bind(R.id.text_view) TextView textView;
  @Bind(R.id.loading) View loadingView;

  @State boolean isLoading = false;
  @State CharSequence data = null;

  private PublishSubject<Boolean> loadingSubject = PublishSubject.create();
  private PublishSubject<CharSequence> dataSubject = PublishSubject.create();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getAppComponent(getActivity()).inject(this);
    Icepick.restoreInstanceState(this, savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_hello1, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    presenter.setView(this);

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
      presenter.refresh();
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    presenter.setView(null);
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

  @OnClick(R.id.refresh) public void refresh() {
    presenter.refresh();
  }

  @OnClick(R.id.clear_memory_cache) public void clearMemoryCache() {
    presenter.clearMemoryCache();
  }

  @OnClick(R.id.clear_memory_and_disk_cache) public void clearMemoryAndDiskCache() {
    presenter.clearMemoryAndDiskCache();
  }
}
