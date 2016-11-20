package com.example.kringlan.sololev.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.adapter.OrderAdapter;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.util.SharedPrefsHelper;

public class OrderListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Order[] orders = new Order[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        orders[0] = new Order(new Customer("Sven", "070-33558899", "Svinstigen 33"));

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OrderAdapter(orders);
        recyclerView.setAdapter(adapter);

    }

    public void addNewOrder(View view) {
        DBHelper db = new DBHelper(this);
        db.addOrder(new Order(new Customer("Sven", "070-33558899", "Svinstigen 33")));
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
