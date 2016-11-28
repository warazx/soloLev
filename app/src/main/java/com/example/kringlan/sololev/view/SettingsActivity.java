package com.example.kringlan.sololev.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kringlan.sololev.R;

public class SettingsActivity extends AppCompatActivity {

    public static final int START_VALUE = 10;

    private SeekBar seekBar;
    private TextView newOrderAmount;
    private TextView phoneNumberText;
    private EditText newPhoneNumberEdit;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newOrderAmount = (TextView) findViewById(R.id.settings_activity_new_orders_amount_value);
        seekBar = (SeekBar) findViewById(R.id.settings_activity_new_orders_seekbar);
        phoneNumberText = (TextView) findViewById(R.id.settings_activity_phonenumber_value);
        newPhoneNumberEdit = (EditText) findViewById(R.id.settings_activity_phonenumber_new_value_edit);

        sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        int currentAmountSetting = sharedPref.getInt(getString(R.string.settings_new_orders_amount_status), START_VALUE);
        newOrderAmount.setText(currentAmountSetting + "");
        String currentPhoneNumberSetting = sharedPref.getString(getString(R.string.settings_phonenumber_status), "");
        phoneNumberText.setText(currentPhoneNumberSetting);

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

    public void updatePhoneNumber(View view) {
        String newNumber = newPhoneNumberEdit.getText().toString();
        if(checkNumber(newNumber)) {
            editor.putString(getString(R.string.settings_phonenumber_status), newNumber);
            phoneNumberText.setText(newPhoneNumberEdit.getText().toString());
        }
    }

    private boolean checkNumber(String number) {
        for(int i = 0; i < number.length(); i++) {
            if(!checkChar(number.charAt(i))) return false;
        }
        return true;
    }

    private boolean checkChar(char c) {
        // 0 till 9 och "-".
        return (c >= 48 && c <= 57 || c == 45);
    }
}
