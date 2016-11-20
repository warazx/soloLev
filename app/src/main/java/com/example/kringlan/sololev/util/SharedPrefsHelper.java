package com.example.kringlan.sololev.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.model.Order;

public final class SharedPrefsHelper {

    public void saveSharedPrefs(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(String.valueOf(R.string.order_counter_status), Order.getIdCounter());
        editor.commit();
    }

    public void loadSharedPrefs(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int value = sharedPref.getInt(String.valueOf(R.string.order_counter_status), 1);
        Order.setIdCounter(value);
    }
}
