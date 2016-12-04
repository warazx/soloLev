package com.example.kringlan.sololev.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kringlan.sololev.adapter.OrderAdapter;
import com.example.kringlan.sololev.model.Order;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class FindOrderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "SCANNER_VIEW";

    private Order order;

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_find_order);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        String value = result.getText();
        Log.v(TAG, value); // Prints scan results
        String typeOfBarcode = result.getBarcodeFormat().toString();
        Log.v(TAG, typeOfBarcode); // Prints the scan format (qrcode, pdf417 etc.)

        if(!result.getText().isEmpty()) {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(OrderAdapter.ORDER_ID, result.getText());
            startActivity(intent);
        }

        // If you would like to resume scanning, call this method below:
        scannerView.resumeCameraPreview(this);
    }
}
