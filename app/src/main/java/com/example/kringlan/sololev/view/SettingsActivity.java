package com.example.kringlan.sololev.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;

public class SettingsActivity extends AppCompatActivity {

    public static final int START_VALUE = 10;

    private SeekBar seekBar;
    private TextView newOrderAmount;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newOrderAmount = (TextView) findViewById(R.id.settings_activity_new_orders_amount_value);
        seekBar = (SeekBar) findViewById(R.id.settings_activity_new_orders_seekbar);
        sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        int currentSetting = sharedPref.getInt(getString(R.string.settings_new_orders_amount_status), START_VALUE);
        newOrderAmount.setText(currentSetting + "");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                newOrderAmount.setText(String.valueOf(START_VALUE + i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onPause() {
        editor.putInt(getString(R.string.settings_new_orders_amount_status), Integer.parseInt(newOrderAmount.getText().toString()));
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        int currentSetting = sharedPref.getInt(getString(R.string.settings_new_orders_amount_status), START_VALUE);
        newOrderAmount.setText(currentSetting + "");
        seekBar.setProgress(currentSetting - START_VALUE);
        super.onResume();
    }
}
