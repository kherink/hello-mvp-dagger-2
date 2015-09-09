package com.example.bradcampbell;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.bradcampbell.ui.MainActivity;

public class MainActivityTestRule extends ActivityTestRule<MainActivity> {
    public MainActivityTestRule() {
        super(MainActivity.class);
    }

    @Override protected Intent getActivityIntent() {
        Intent intent = super.getActivityIntent();
        intent.putExtra(MainActivity.FLAG_NO_FRAGMENT, true);
        return intent;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.root, fragment)
                .commit();

        // Wait for the fragment to be committed
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();
    }
}
