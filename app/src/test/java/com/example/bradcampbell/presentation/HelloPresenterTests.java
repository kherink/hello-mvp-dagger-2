package com.example.bradcampbell.presentation;

import com.example.bradcampbell.AppComponent;
import com.example.bradcampbell.BuildConfig;
import com.example.bradcampbell.DaggerAppComponent;
import com.example.bradcampbell.MockAppModule;
import com.example.bradcampbell.TestApp;
import com.example.bradcampbell.domain.HelloEntity;
import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.ui.DaggerHelloComponent;
import com.example.bradcampbell.ui.HelloComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.schedulers.TestScheduler;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        application = TestApp.class)
public class HelloPresenterTests {
    private HelloPresenter presenter;
    private HelloModel mockModel;
    private HelloView mockView;

    @Before public void setup() {
        TestApp app = (TestApp) RuntimeEnvironment.application;

        mockModel = mock(HelloModel.class);
        mockView = mock(HelloView.class);

        MockAppModule mockAppModule = new MockAppModule(app);
        mockAppModule.setOverrideHelloModel(mockModel);

        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(mockAppModule)
                .build();

        HelloComponent helloComponent = DaggerHelloComponent.builder()
                .appComponent(appComponent)
                .build();

        presenter = helloComponent.getPresenter();
        presenter.bindView(mockView);
    }

    @Test public void testLoadingIsCalledCorrectly() {
        TestScheduler testScheduler = new TestScheduler();
        Observable<HelloEntity> result = just(HelloEntity.create(0, 0))
                .subscribeOn(testScheduler);
        when(mockModel.getValue()).thenReturn(result);

        presenter.load();

        verify(mockView, times(1)).showLoading();
        verify(mockView, never()).hideLoading();
        verify(mockView, never()).display(anyString());

        testScheduler.triggerActions();

        verify(mockView, times(1)).display("0");
        verify(mockView, times(1)).hideLoading();
    }
}
