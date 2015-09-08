package com.example.bradcampbell;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

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
}
