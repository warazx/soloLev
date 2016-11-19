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
    private Button loginButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = new User("te", "st");

        usernameEditText = (EditText) findViewById(R.id.activity_login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.activity_login_password_edittext);
        loginButton = (Button) findViewById(R.id.activity_login_login_btn);
    }

    public void logIntoApp(View view) {
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
        if(!usernameEditText.getText().toString().equals(user.getUsername())) {
            writeNewUserToDB(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        } else {
            Toast.makeText(this, R.string.toast_add_user_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNewUserToDB(String username, String password) {
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addUser(username, password);
    }
}
