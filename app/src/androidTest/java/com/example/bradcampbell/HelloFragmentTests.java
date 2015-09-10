package com.example.bradcampbell;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.example.bradcampbell.domain.HelloEntity;
import com.example.bradcampbell.domain.HelloModel;
import com.example.bradcampbell.ui.HelloFragment;
import com.example.bradcampbell.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;
import rx.schedulers.TestScheduler;

import static android.support.test.InstrumentationRegistry.getTargetContext;
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
    @Rule public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<>(MainActivity.class, false, false);

    private HelloModel mockHelloModel;

    @Before public void setup() throws Throwable {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestApp app = (TestApp) instrumentation.getTargetContext().getApplicationContext();

        // Set up application module
        mockHelloModel = mock(HelloModel.class);
        MockAppModule mockAppModule = new MockAppModule(app);
        mockAppModule.setOverrideHelloModel(mockHelloModel);
        mockAppModule.setOverrideLayoutInflaterFactory((parent, name, context, attrs) -> {
            View result = null;
            // ProgressBar animates forever which keeps the main thread from being idle
            // and hence blocks Espresso from working. This is true even if you disable
            // animations on the device (testing on Android L and above it seems). I have
            // added a hook to replace any ProgressBar with a plain old View instance
            // to avoid the animation issues.
            if ("ProgressBar".equals(name)) {
                result = new View(context, attrs);
            }
            // We have overridden the Activity Delegate LayoutFactory, so we need to call
            // back into it
            if (result == null && context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                AppCompatDelegate delegate = activity.getDelegate();
                result = delegate.createView(parent, name, context, attrs);
            }
            return result;
        });
        app.setOverrideModule(mockAppModule);

        // Launch main activity
        Intent mainActivityIntent = MainActivity.getStartIntent(getTargetContext(), false);
        main.launchActivity(mainActivityIntent);
    }

    @After public void tearDown() {
        App.clearAppComponent(main.getActivity());
    }

    @Test public void testLoadingDisplaysCorrectly() throws Throwable {
        // Build mock data
        TestScheduler testScheduler = new TestScheduler();
        Observable<HelloEntity> result = just(HelloEntity.create(0, 0L))
                .subscribeOn(testScheduler)
                .observeOn(mainThread());
        when(mockHelloModel.getValue()).thenReturn(result);

        // Add fragment
        FragmentManager fragmentManager = main.getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.root, new HelloFragment())
                .commit();

        // Wait for the fragment to be committed
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        // Check loading is showing
        onView(withId(R.id.loading)).check(matches(isDisplayed()));
        onView(withId(R.id.text_view)).check(matches(not(isDisplayed())));

        screenshot(main.getActivity(), "loading");

        // Trigger onNext/onCompleted
        testScheduler.triggerActions();

        // Check loading is hidden
        onView(withId(R.id.loading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.text_view)).check(matches(isDisplayed()));

        screenshot(main.getActivity(), "result");
    }

    @Test public void ensureOnlyOneRequestCanBeExecutedAtATime() throws Throwable {
        // Build mock data
        TestScheduler testScheduler = new TestScheduler();
        Observable<HelloEntity> result = just(HelloEntity.create(0, 0L))
                .subscribeOn(testScheduler)
                .observeOn(mainThread());
        when(mockHelloModel.getValue()).thenReturn(result);

        // Add fragment
        FragmentManager fragmentManager = main.getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.root, new HelloFragment())
                .commit();

        // Wait for the fragment to be committed
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        // Verify the initial getValue call was made
        verify(mockHelloModel, times(1)).getValue();

        // Click while the call is still in progress and ensure that no additional call was made
        onView(withId(R.id.load)).perform(click());
        verify(mockHelloModel, times(1)).getValue();

        // Trigger onNext/onCompleted
        testScheduler.triggerActions();

        // Ensure another click will trigger another call
        onView(withId(R.id.load)).perform(click());
        verify(mockHelloModel, times(2)).getValue();

        screenshot(main.getActivity(), "end");
    }
}
