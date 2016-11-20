package com.example.kringlan.sololev.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;

public final class SharedPrefsHelper {

    public static void saveSharedPrefs(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.order_counter_status), Order.getIdCounter());
        editor.putInt(activity.getString(R.string.customer_counter_status), Customer.getIdCounter());
        editor.apply();
    }

    public static void loadSharedPrefs(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int orderCounter = sharedPref.getInt(activity.getString(R.string.order_counter_status), Order.getIdCounter());
        int customerCounter = sharedPref.getInt(activity.getString(R.string.customer_counter_status), Customer.getIdCounter());
        Order.setIdCounter(orderCounter);
        Customer.setIdCounter(customerCounter);
    }
}
