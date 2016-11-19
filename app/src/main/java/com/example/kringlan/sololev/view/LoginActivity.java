package com.example.kringlan.sololev.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kringlan.sololev.R;
import com.example.kringlan.sololev.database.DBHelper;
import com.example.kringlan.sololev.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.activity_login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.activity_login_password_edittext);
    }

    public void logIntoApp(View view) {
        DBHelper db = new DBHelper(this);
        User user = db.findUser(usernameEditText.getText().toString());

        if(usernameEditText.getText().toString().equals(user.getUsername())) {
            if(passwordEditText.getText().toString().equals(user.getPassword())) {
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

        if(db.addUser(username, password)) {
            logIntoApp(view);
        } else {
            Toast.makeText(this, R.string.toast_add_user_error_message, Toast.LENGTH_SHORT).show();
        }
    }
}
