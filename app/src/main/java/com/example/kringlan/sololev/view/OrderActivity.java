package com.example.kringlan.sololev.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.adapter.OrderAdapter;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.util.SharedPrefsHelper;
import com.example.kringlan.sololev.util.TimeConverter;

public class OrderActivity extends AppCompatActivity {

    private TextView orderIdText;
    private TextView addressText;
    private TextView nameText;
    private TextView phoneText;
    private Button deliverBtn;
    private TextView deliveredDate;

    private LinearLayout llDelivered;
    private LinearLayout llNotDelivered;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderIdText = (TextView) findViewById(R.id.order_activity_order_id_value);
        addressText = (TextView) findViewById(R.id.order_activity_costumer_address_value);
        nameText = (TextView) findViewById(R.id.order_activity_costumer_name_value);
        phoneText = (TextView) findViewById(R.id.order_activity_costumer_phone_value);
        llDelivered = (LinearLayout) findViewById(R.id.ll_delivered);
        llNotDelivered = (LinearLayout) findViewById(R.id.ll_not_delivered);
        deliveredDate = (TextView) findViewById(R.id.order_activity_delivered_date_value);
        deliverBtn = (Button) findViewById(R.id.order_activity_deliver_btn);

        Intent intent = getIntent();
        String id = intent.getStringExtra(OrderAdapter.ORDER_ID);

        DBHelper db = new DBHelper(this);
        order = db.findOrder(id);
        Customer customer = order.getCustomer();

        toggleView();

        orderIdText.setText(order.getOrderID() + "");
        addressText.setText(customer.getAddress());
        nameText.setText(customer.getName());
        phoneText.setText(customer.getPhoneNumber());
        deliveredDate.setText(TimeConverter.toString(order.getDeliveredDate()));
    }

    private void toggleView() {
        if(order.isDelivered()) {
            llNotDelivered.setVisibility(View.GONE);
            llDelivered.setVisibility(View.VISIBLE);
        } else {
            llNotDelivered.setVisibility(View.VISIBLE);
            llDelivered.setVisibility(View.GONE);
        }
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

    public void deliverOrder(View view) {
        order.deliver();
        DBHelper db = new DBHelper(this);
        db.setOrderToDelivered(order);
        deliveredDate.setText(TimeConverter.toString(order.getDeliveredDate()));
        toggleView();
    }
}
