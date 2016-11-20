package com.example.kringlan.sololev.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.util.SharedPrefsHelper;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPrefsHelper.saveSharedPrefs(this);
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        SharedPrefsHelper.loadSharedPrefs(this);
    }
}
