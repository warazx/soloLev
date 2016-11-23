package com.example.kringlan.sololev.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.model.User;
import com.example.kringlan.sololev.util.GPSTracker;
import com.example.kringlan.sololev.util.GenerateCustomer;
import com.example.kringlan.sololev.util.SharedPrefsHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText usernameEditText;
    private EditText passwordEditText;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.activity_login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.activity_login_password_edittext);

        GenerateCustomer.add(10, this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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

    public void logIntoApp(View view) {
        DBHelper db = new DBHelper(this);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        User user = db.findUser(username);

        if (user != null) {
            if (password.equals(user.getPassword())) {
                Intent intent = new Intent(this, OrderListActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, R.string.toast_login_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    public void addUserToDatabase(View view) {
        DBHelper db = new DBHelper(this);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (db.addUser(username, password)) {
            Toast.makeText(this, R.string.toast_add_user_success_message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.toast_add_user_error_message, Toast.LENGTH_SHORT).show();
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
}
