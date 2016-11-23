package com.example.kringlan.sololev.util;

import android.app.Activity;

import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Order;

import java.util.Random;

public final class GenerateOrders {
    private static Random rand = new Random();

    public static void add(int amount, Activity activity) {
        DBHelper dbHelper = new DBHelper(activity);

        int numOrders = dbHelper.getDeliveredOrders().length + dbHelper.getUndeliveredOrders().length;
        Order.setIdCounter(numOrders);

        Order order;
        for(int i = 0; i < amount; i++) {
            order = new Order(dbHelper.findCustomer(rand.nextInt(10)));
            dbHelper.addOrder(order);
        }
    }
}
