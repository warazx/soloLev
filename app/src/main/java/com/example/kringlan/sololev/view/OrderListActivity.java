package com.example.kringlan.sololev.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.adapter.OrderAdapter;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.util.GenerateOrders;
import com.example.kringlan.sololev.util.SharedPrefsHelper;

public class OrderListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Order[] orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        initOrders();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OrderAdapter(orders);
        recyclerView.setAdapter(adapter);
    }

    public void addNewOrder(View view) {
        GenerateOrders.add(10, this);
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
        loadUndeliveredOrders();
    }

    public void loadUndeliveredOrders(View view) {
        loadUndeliveredOrders();
    }

    public void loadUndeliveredOrders() {
        DBHelper db = new DBHelper(this);
        orders = db.getUndeliveredOrders();
        db.close();
        RecyclerView.Adapter newAdapter = new OrderAdapter(orders);
        recyclerView.swapAdapter(newAdapter, true);
    }

    private void initOrders() {
        DBHelper db = new DBHelper(this);
        orders = db.getUndeliveredOrders();
        db.close();
    }


    public void loadDeliveredOrders(View view) {
        DBHelper db = new DBHelper(this);
        orders = db.getDeliveredOrders();
        db.close();
        RecyclerView.Adapter newAdapter = new OrderAdapter(orders);
        recyclerView.swapAdapter(newAdapter, true);
    }
}
