package com.example.kringlan.sololev.view;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.adapter.OrderAdapter;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.util.GPSTracker;
import com.example.kringlan.sololev.util.GenerateOrders;
import com.example.kringlan.sololev.util.SharedPrefsHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class OrderListActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean isShowingDeliveredOrders;

    private MenuItem deliveredOrdersOption;
    private MenuItem undeliveredOrdersOption;

    private GoogleApiClient googleApiClient;

    private SharedPreferences sharedPref;

    private Order[] orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        isShowingDeliveredOrders = false;

        initOrders();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OrderAdapter(orders);
        recyclerView.setAdapter(adapter);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
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
        loadUndeliveredOrders();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        deliveredOrdersOption = menu.findItem(R.id.actionbar_delivered_orders);
        undeliveredOrdersOption = menu.findItem(R.id.actionbar_undelivered_orders);
        deliveredOrdersOption.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_add_orders:
                addNewOrder();
                return true;
            case R.id.actionbar_delivered_orders:
                toggleOrders();
                return true;
            case R.id.actionbar_undelivered_orders:
                toggleOrders();
                return true;
            case R.id.actionbar_settings:
                goToSettings();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewOrder() {
        int amount = sharedPref.getInt(getString(R.string.settings_new_orders_amount_status), SettingsActivity.START_VALUE);
        GenerateOrders.add(amount, this);
        isShowingDeliveredOrders = false;
        loadUndeliveredOrders();
    }

    private void toggleOrders() {
        if(isShowingDeliveredOrders) {
            loadUndeliveredOrders();
            isShowingDeliveredOrders = false;
        } else {
            loadDeliveredOrders();
            isShowingDeliveredOrders = true;
        }
        toggleMenuItems();
    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void loadUndeliveredOrders() {
        DBHelper db = new DBHelper(this);
        orders = db.getUndeliveredOrders();
        db.close();
        RecyclerView.Adapter newAdapter = new OrderAdapter(orders);
        recyclerView.swapAdapter(newAdapter, true);
        toggleMenuItems();
    }

    private void toggleMenuItems() {
        if(deliveredOrdersOption != null && undeliveredOrdersOption != null) {
            if(isShowingDeliveredOrders) {
                deliveredOrdersOption.setVisible(false);
                undeliveredOrdersOption.setVisible(true);
            } else {
                deliveredOrdersOption.setVisible(true);
                undeliveredOrdersOption.setVisible(false);
            }
        }
    }

    private void initOrders() {
        DBHelper db = new DBHelper(this);
        orders = db.getUndeliveredOrders();
        db.close();
    }


    public void loadDeliveredOrders() {
        DBHelper db = new DBHelper(this);
        orders = db.getDeliveredOrders();
        db.close();
        RecyclerView.Adapter newAdapter = new OrderAdapter(orders);
        recyclerView.swapAdapter(newAdapter, true);
        toggleMenuItems();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need permission to use service", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        GPSTracker.setLastLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
