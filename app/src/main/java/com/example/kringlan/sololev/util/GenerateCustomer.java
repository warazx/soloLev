package com.example.kringlan.sololev.util;

import android.app.Activity;

import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Customer;

import java.util.Random;

public final class GenerateCustomer {
    private static Random rand = new Random();

    public static String[] names = {"Stephan", "Larhonda", "Karlene", "Johanna", "Bernarda",
            "Nikia", "Jasper", "Minh", "Tifany", "Reyes"};

    public static String[] addresses = {"Tegnérsgatan 14B", "Volrat Thamsgatan 4",
            "Södra Viktoriagatan 42", "Odinsgatan 9", "Smörbollsgatan 6B", "Skillnadsgatan 19",
            "Lagmansgatan 8B", "Tenorgatan 2", "J A Pripps gata 2", "Kungsladugårdsgatan 21"};

    public static void add(int amount, Activity activity) {
        DBHelper db = new DBHelper(activity);



        String name;
        String address;
        String phone;

        for(int i = 0; i < amount; i++) {
            name = names[rand.nextInt(names.length)];
            address = addresses[rand.nextInt(addresses.length)];
            phone = "070-" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10);
            Customer customer = new Customer(name, address, phone);
            db.addCustomer(customer);
        }
        db.close();
    }
}
