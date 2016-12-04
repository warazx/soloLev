package com.example.kringlan.sololev.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.adapter.OrderAdapter;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.util.GPSTracker;
import com.example.kringlan.sololev.util.SharedPrefsHelper;
import com.example.kringlan.sololev.util.DataConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OrderActivity
        extends AppCompatActivity
        implements  GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {

    public static final String LATITUDE_KEY = "latitude";
    public static final String LONGITUDE_KEY = "longitude";

    private TextView orderIdText;
    private TextView addressText;
    private TextView nameText;
    private TextView phoneText;
    private TextView deliveredDate;
    private SharedPreferences sharedPref;
    private LinearLayout llDelivered;
    private LinearLayout llNotDelivered;
    private MenuItem goToSettingsOption;
    private GoogleApiClient googleApiClient;
    private SmsManager smsManager = SmsManager.getDefault();
    private Order order;
    private GoogleMap googleMap;
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initVariableMapping();

        Intent intent = getIntent();
        String id = intent.getStringExtra(OrderAdapter.ORDER_ID);

        DBHelper db = new DBHelper(this);
        order = db.findOrder(id);
        Customer customer = order.getCustomer();

        toggleView();
        initSetValues(customer);
        initGoogleApiClient();
    }

    private void initVariableMapping() {
        orderIdText = (TextView) findViewById(R.id.order_activity_order_id_value);
        addressText = (TextView) findViewById(R.id.order_activity_costumer_address_value);
        nameText = (TextView) findViewById(R.id.order_activity_costumer_name_value);
        phoneText = (TextView) findViewById(R.id.order_activity_costumer_phone_value);
        llDelivered = (LinearLayout) findViewById(R.id.ll_delivered);
        llNotDelivered = (LinearLayout) findViewById(R.id.ll_not_delivered);
        deliveredDate = (TextView) findViewById(R.id.order_activity_delivered_date_value);
        sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initSetValues(Customer customer) {
        orderIdText.setText(order.getOrderID() + "");
        addressText.setText(customer.getAddress());
        nameText.setText(customer.getName());
        phoneText.setText(customer.getPhoneNumber());
        deliveredDate.setText(DataConverter.longToDateString(order.getDeliveredDate()));
    }

    private void initGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void toggleView() {
        if(order.isDelivered()) {
            mapFragment.getMapAsync(this);
            llNotDelivered.setVisibility(View.GONE);
            llDelivered.setVisibility(View.VISIBLE);
        } else {
            llNotDelivered.setVisibility(View.VISIBLE);
            llDelivered.setVisibility(View.GONE);
        }
    }

    public void deliverOrder(View view) {
        order.deliver();
        DBHelper db = new DBHelper(this);
        db.setOrderToDelivered(order);
        deliveredDate.setText(DataConverter.longToDateString(order.getDeliveredDate()));
        sendSMS();
        toggleView();
    }

    private void sendSMS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need permission to use service", Toast.LENGTH_SHORT).show();ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        String phoneNumber = sharedPref.getString(getString(R.string.settings_phonenumber_status), "");
        String message = String.format(getString(R.string.order_activity_sms_message), order.getOrderID());

        smsManager.sendTextMessage(phoneNumber, "", message, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        goToSettingsOption = menu.findItem(R.id.actionbar_settings);

        goToSettingsOption.setVisible(true);

        return true;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need permission to use service", Toast.LENGTH_SHORT).show();ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

    public void goToMap(View view) {
        String latitude = String.valueOf(order.getDeliveredLat());
        String longitude = String.valueOf(order.getDeliveredLong());

        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng deliveredPos = new LatLng(order.getDeliveredLat(), order.getDeliveredLong());
        googleMap.addMarker(new MarkerOptions().position(deliveredPos).title(order.getCustomer().getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deliveredPos, 16));
    }
}
