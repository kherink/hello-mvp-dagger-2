package com.example.bradcampbell;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.example.bradcampbell.domain.HelloEntity;
import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.ui.HelloFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;
import rx.schedulers.TestScheduler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.squareup.spoon.Spoon.screenshot;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HelloFragmentTests {
    @Rule public final MainActivityTestRule main = new MainActivityTestRule();

    private HelloModel mockHelloModel;

    @Before public void setup() throws Throwable {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        TestApp app = (TestApp) instrumentation.getTargetContext().getApplicationContext();

        mockHelloModel = mock(HelloModel.class);

        MockAppModule mockAppModule = new MockAppModule(app);
        mockAppModule.setOverrideHelloModel(mockHelloModel);

        app.setOverrideModule(mockAppModule);

        // ProgressBar animates forever which keeps the main thread from being idle
        // and hence blocks Espresso from working. This is true even if you disable
        // animations on the device (testing on Android L and above it seems). I have
        // added a hook to replace any ProgressBar with a plain old View instance
        // to avoid the animation issues.
        main.getActivity().setLayoutInflaterHook((parent, name, context, attrs) -> {
            if ("ProgressBar".equals(name)) {
                return new View(main.getActivity(), attrs);
            }
            return null;
        });
    }

    @After public void tearDown() {
        App.clearAppComponent(main.getActivity());
    }

    @Test public void testLoadingDisplaysCorrectly() throws Throwable {
        TestScheduler testScheduler = new TestScheduler();
        Observable<HelloEntity> result = just(HelloEntity.create(0, 0L))
                .subscribeOn(testScheduler)
                .observeOn(mainThread());
        when(mockHelloModel.getValue()).thenReturn(result);

        main.setFragment(new HelloFragment());

        onView(withId(R.id.loading)).check(matches(isDisplayed()));
        onView(withId(R.id.text_view)).check(matches(not(isDisplayed())));

        screenshot(main.getActivity(), "loading");

        testScheduler.triggerActions();

        onView(withId(R.id.loading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_view)).check(matches(isDisplayed()));

        screenshot(main.getActivity(), "result");
    }

    @Test public void ensureOnlyOneRequestCanBeExecutedAtATime() throws Throwable {
        TestScheduler testScheduler = new TestScheduler();
        Observable<HelloEntity> result = just(HelloEntity.create(0, 0L))
                .subscribeOn(testScheduler)
                .observeOn(mainThread());
        when(mockHelloModel.getValue()).thenReturn(result);

        main.setFragment(new HelloFragment());

        verify(mockHelloModel, times(1)).getValue();
        onView(withId(R.id.load)).perform(click());
        verify(mockHelloModel, times(1)).getValue();

        testScheduler.triggerActions();

        onView(withId(R.id.load)).perform(click());
        verify(mockHelloModel, times(2)).getValue();

        screenshot(main.getActivity(), "end");
    }
}
