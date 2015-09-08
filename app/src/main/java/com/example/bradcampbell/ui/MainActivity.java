package com.example.bradcampbell.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.example.bradcampbell.R;

public class MainActivity extends AppCompatActivity {
    public static final String FLAG_NO_FRAGMENT = "noFragment";

    private LayoutInflaterFactory layoutInflaterHook;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        boolean noFragment = intent.getBooleanExtra(FLAG_NO_FRAGMENT, false);
        if (savedInstanceState == null && !noFragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root, new HelloFragment())
                    .commit();
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        if (layoutInflaterHook != null) {
            view = layoutInflaterHook.onCreateView(parent, name, context, attrs);
        }
        return view != null ? view : super.onCreateView(parent, name, context, attrs);
    }

    public void setLayoutInflaterHook(LayoutInflaterFactory layoutInflaterHook) {
        this.layoutInflaterHook = layoutInflaterHook;
    }
}
